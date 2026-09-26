-- FUNCTIONS

CREATE OR REPLACE FUNCTION get_favourite_genre (
    p_user_id IN INT
)
RETURN INT AS
    v_genre_id INT;
BEGIN
    SELECT id_genre
    INTO v_genre_id
    FROM (
        SELECT m.id_genre, COUNT(*) AS view_count
        FROM MOVIES m
        JOIN MOVIE_VERSIONS mv
            ON m.id_movie = mv.id_movie
        JOIN VIEWS v
            ON mv.id_version = v.id_version
        WHERE v.id_user = p_user_id
        GROUP BY m.id_genre
        ORDER BY view_count DESC
    )
    WHERE ROWNUM = 1;

    RETURN v_genre_id;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
END;
/


-- PROCEDURES

CREATE OR REPLACE PROCEDURE get_recommendations (
    p_user_id IN INT,
    p_rec1 OUT VARCHAR2,
    p_rec2 OUT VARCHAR2,
    p_rec3 OUT VARCHAR2
) AS
    v_similar_user_id INT;

    TYPE t_titles IS TABLE OF VARCHAR2(150);

    v_titles t_titles := t_titles();

    v_fallback_genre INT;

BEGIN
    p_rec1 := 'N/A';
    p_rec2 := 'N/A';
    p_rec3 := 'N/A';

    BEGIN
        SELECT similar_user
        INTO v_similar_user_id
        FROM (
            SELECT
                r2_user AS similar_user,
                COUNT(*) AS similitudine
            FROM (
                SELECT mv.id_movie
                FROM REVIEWS rev
                JOIN VIEWS v
                    ON rev.id_view = v.id_view
                JOIN MOVIE_VERSIONS mv
                    ON v.id_version = mv.id_version
                WHERE v.id_user = p_user_id
                  AND (
                      rev.rating >= 4
                      OR rev.sentiment_score = 'Positive'
                  )
            ) my_likes
            JOIN (
                SELECT
                    v.id_user AS r2_user,
                    mv.id_movie
                FROM REVIEWS rev
                JOIN VIEWS v
                    ON rev.id_view = v.id_view
                JOIN MOVIE_VERSIONS mv
                    ON v.id_version = mv.id_version
                WHERE v.id_user != p_user_id
                  AND (
                      rev.rating >= 4
                      OR rev.sentiment_score = 'Positive'
                  )
            ) other_likes
                ON my_likes.id_movie = other_likes.id_movie
            GROUP BY r2_user
            ORDER BY similitudine DESC
        )
        WHERE ROWNUM = 1;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_similar_user_id := NULL;
    END;


    IF v_similar_user_id IS NOT NULL THEN

        SELECT title
        BULK COLLECT INTO v_titles
        FROM (
            SELECT m.title
            FROM REVIEWS rev
            JOIN VIEWS v
                ON rev.id_view = v.id_view
            JOIN MOVIE_VERSIONS mv
                ON v.id_version = mv.id_version
            JOIN MOVIES m
                ON mv.id_movie = m.id_movie
            WHERE v.id_user = v_similar_user_id
              AND (
                  rev.rating >= 4
                  OR rev.sentiment_score = 'Positive'
              )
              AND m.id_movie NOT IN (
                  SELECT mv_sub.id_movie
                  FROM VIEWS v_sub
                  JOIN MOVIE_VERSIONS mv_sub
                      ON v_sub.id_version = mv_sub.id_version
                  WHERE v_sub.id_user = p_user_id
              )
            ORDER BY rev.rating DESC
        )
        WHERE ROWNUM <= 3;

    END IF;


    IF v_titles.COUNT < 3 THEN

        v_fallback_genre := get_favourite_genre(p_user_id);

        IF v_fallback_genre IS NOT NULL THEN

            SELECT title
            BULK COLLECT INTO v_titles
            FROM (
                SELECT title
                FROM MOVIES
                WHERE id_genre = v_fallback_genre
                  AND id_movie NOT IN (
                      SELECT mv_sub.id_movie
                      FROM VIEWS v_sub
                      JOIN MOVIE_VERSIONS mv_sub
                          ON v_sub.id_version = mv_sub.id_version
                      WHERE v_sub.id_user = p_user_id
                  )
                ORDER BY rating_mediu DESC
            )
            WHERE ROWNUM <= 3;

        ELSE

            SELECT title
            BULK COLLECT INTO v_titles
            FROM (
                SELECT title
                FROM MOVIES
                ORDER BY rating_mediu DESC
            )
            WHERE ROWNUM <= 3;

        END IF;

    END IF;


    IF v_titles.EXISTS(1) THEN
        p_rec1 := v_titles(1);
    END IF;

    IF v_titles.EXISTS(2) THEN
        p_rec2 := v_titles(2);
    END IF;

    IF v_titles.EXISTS(3) THEN
        p_rec3 := v_titles(3);
    END IF;

END;
/


-- TRIGGERS

CREATE OR REPLACE TRIGGER trg_set_view_status
BEFORE INSERT OR UPDATE ON VIEWS
FOR EACH ROW
DECLARE
    v_duration INT;

BEGIN
    SELECT m.movie_duration
    INTO v_duration
    FROM MOVIES m
    JOIN MOVIE_VERSIONS mv
        ON m.id_movie = mv.id_movie
    WHERE mv.id_version = :NEW.id_version;


    IF :NEW.watched_minutes = v_duration THEN
        :NEW.view_status := 'COMPLETED';
        :NEW.watched_minutes := v_duration;
    ELSE
        :NEW.view_status := 'IN_PROGRESS';
    END IF;

END;
/


CREATE OR REPLACE TRIGGER trg_set_sentiment_score
BEFORE INSERT OR UPDATE OF comment_text, rating ON REVIEWS
FOR EACH ROW
DECLARE
    v_text VARCHAR2(1000);
    v_current_score INT := 0;

BEGIN
    IF :NEW.comment_text IS NULL THEN

        IF :NEW.rating >= 4 THEN
            :NEW.sentiment_score := 'Positive';

        ELSIF :NEW.rating = 3 THEN
            :NEW.sentiment_score := 'Neutral';

        ELSE
            :NEW.sentiment_score := 'Negative';
        END IF;


    ELSE

        v_text := LOWER(:NEW.comment_text);


        IF INSTR(v_text, 'not good') > 0 THEN
            v_current_score := v_current_score - 1;
            v_text := REPLACE(v_text, 'not good', ' ');
        END IF;

        IF INSTR(v_text, 'not great') > 0 THEN
            v_current_score := v_current_score - 1;
            v_text := REPLACE(v_text, 'not great', ' ');
        END IF;

        IF INSTR(v_text, 'not the best') > 0 THEN
            v_current_score := v_current_score - 1;
            v_text := REPLACE(v_text, 'not the best', ' ');
        END IF;

        IF INSTR(v_text, 'not excellent') > 0 THEN
            v_current_score := v_current_score - 1;
            v_text := REPLACE(v_text, 'not excellent', ' ');
        END IF;

        IF INSTR(v_text, 'do not recommend') > 0 THEN
            v_current_score := v_current_score - 2;
            v_text := REPLACE(v_text, 'do not recommend', ' ');
        END IF;

        IF INSTR(v_text, 'waste of time') > 0 THEN
            v_current_score := v_current_score - 2;
            v_text := REPLACE(v_text, 'waste of time', ' ');
        END IF;


        IF INSTR(v_text, 'great') > 0 THEN
            v_current_score := v_current_score + 1;
        END IF;

        IF INSTR(v_text, 'excellent') > 0 THEN
            v_current_score := v_current_score + 1;
        END IF;

        IF INSTR(v_text, 'amazing') > 0 THEN
            v_current_score := v_current_score + 1;
        END IF;

        IF INSTR(v_text, 'masterpiece') > 0 THEN
            v_current_score := v_current_score + 2;
        END IF;

        IF INSTR(v_text, 'loved') > 0 THEN
            v_current_score := v_current_score + 1;
        END IF;

        IF INSTR(v_text, 'perfect') > 0 THEN
            v_current_score := v_current_score + 1;
        END IF;


        IF INSTR(v_text, 'boring') > 0 THEN
            v_current_score := v_current_score - 1;
        END IF;

        IF INSTR(v_text, 'terrible') > 0 THEN
            v_current_score := v_current_score - 1;
        END IF;

        IF INSTR(v_text, 'awful') > 0 THEN
            v_current_score := v_current_score - 1;
        END IF;

        IF INSTR(v_text, 'worst') > 0 THEN
            v_current_score := v_current_score - 2;
        END IF;

        IF INSTR(v_text, 'bad') > 0 THEN
            v_current_score := v_current_score - 1;
        END IF;

        IF INSTR(v_text, 'disappointing') > 0 THEN
            v_current_score := v_current_score - 1;
        END IF;


        IF :NEW.rating = 5 THEN
            v_current_score := v_current_score + 2;

        ELSIF :NEW.rating = 4 THEN
            v_current_score := v_current_score + 1;

        ELSIF :NEW.rating = 2 THEN
            v_current_score := v_current_score - 1;

        ELSIF :NEW.rating = 1 THEN
            v_current_score := v_current_score - 2;
        END IF;


        IF v_current_score > 0 THEN
            :NEW.sentiment_score := 'Positive';

        ELSIF v_current_score < 0 THEN
            :NEW.sentiment_score := 'Negative';

        ELSE
            :NEW.sentiment_score := 'Neutral';
        END IF;

    END IF;

END;
/


CREATE OR REPLACE TRIGGER trg_recalculate_rating
FOR INSERT OR UPDATE OR DELETE ON REVIEWS
COMPOUND TRIGGER

    TYPE t_movie_ids IS TABLE OF INT;

    v_movies t_movie_ids := t_movie_ids();


    AFTER EACH ROW IS
        v_movie_id INT;

    BEGIN

        IF DELETING THEN

            SELECT mv.id_movie
            INTO v_movie_id
            FROM VIEWS v
            JOIN MOVIE_VERSIONS mv
                ON v.id_version = mv.id_version
            WHERE v.id_view = :OLD.id_view;

        ELSE

            SELECT mv.id_movie
            INTO v_movie_id
            FROM VIEWS v
            JOIN MOVIE_VERSIONS mv
                ON v.id_version = mv.id_version
            WHERE v.id_view = :NEW.id_view;

        END IF;


        v_movies.EXTEND;
        v_movies(v_movies.LAST) := v_movie_id;

    END AFTER EACH ROW;


    AFTER STATEMENT IS

    BEGIN

        FOR i IN 1 .. v_movies.COUNT LOOP

            UPDATE MOVIES m
            SET rating_mediu = (
                SELECT NVL(AVG(r.rating), 0)
                FROM REVIEWS r
                JOIN VIEWS v
                    ON r.id_view = v.id_view
                JOIN MOVIE_VERSIONS mv
                    ON v.id_version = mv.id_version
                WHERE mv.id_movie = v_movies(i)
            )
            WHERE m.id_movie = v_movies(i);

        END LOOP;

    END AFTER STATEMENT;

END trg_recalculate_rating;
/
