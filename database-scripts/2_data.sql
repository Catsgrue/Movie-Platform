DECLARE
TYPE string_array IS TABLE OF VARCHAR2(1000);
    TYPE num_array IS TABLE OF INT;

    v_genres string_array := string_array('Action', 'Comedy', 'Drama', 'Horror', 'Sci-Fi', 'Documentary', 'Thriller', 'Romance', 'Animation', 'Adventure', 'Mystery', 'Crime', 'Fantasy', 'History', 'Musical');

    v_movies string_array := string_array('The Dark Knight', 'The Avengers', 'Die Hard', 'Superbad', 'The Hangover', 'The Shawshank Redemption', 'Fight Club', 'Forrest Gump', 'The Conjuring', 'A Nightmare on Elm Street', 'Inception', 'The Matrix', 'Interstellar', 'Avatar', 'Free Solo', 'Se7en', 'The Silence of the Lambs', 'Titanic', 'The Notebook', 'Toy Story', 'Spider-Man: Into the Spider-Verse', 'Jurassic Park', 'Prisoners', 'The Godfather', 'Pulp Fiction', 'Harry Potter', 'The Lord of the Rings', 'Gladiator', 'La La Land');

    v_desc string_array := string_array('Bane attacks Gotham City and disrupts its peace.', 'Earths mightiest heroes must come together to stop Loki.', 'An NYPD officer tries to save his wife from a hostage situation.', 'Two seniors are forced to deal with separation anxiety.', 'Three buddies wake up from a bachelor party with no memory.', 'A wrongfully convicted banker forms a friendship with a convict.', 'An office worker and a soap maker form an underground club.', 'The history of the US unfolds from the perspective of an Alabama man.', 'Investigators work to help a family terrorized by a dark presence.', 'A spirit of a slain murderer kills teenagers in their dreams.', 'A thief steals corporate secrets through dream-sharing technology.', 'A hacker discovers life is a simulation run by an AI.', 'Astronauts embark on a mission to find a new home for humanity.', 'A marine on Pandora is torn between orders and protecting the world.', 'A documentary detailing Alex Honnold free solo ascent of El Capitan.', 'Two detectives hunt a killer who uses the seven deadly sins.', 'A cadet is aided by a manipulative cannibal killer.', 'An aristocrat falls in love with a poor artist aboard the Titanic.', 'A poor young man falls in love with a rich young woman.', 'A cowboy doll is profoundly threatened by a new spaceman.', 'Teen Miles Morales becomes the Spider-Man of his universe.', 'An industrialist invites experts to a theme park of cloned dinosaurs.', 'When Kellers daughter goes missing, he takes matters into his own hands.', 'The aging patriarch of a crime dynasty transfers control to his son.', 'The lives of two mob hitmen and a boxer intertwine.', 'An orphaned boy enrolls in a school of wizardry.', 'A meek Hobbit sets out to destroy the One Ring.', 'A former Roman General exacts vengeance against the corrupt emperor.', 'A pianist and an actress fall in love while navigating their careers.');

    v_movie_date string_array := string_array('2008-07-18', '2012-05-04', '1988-07-15', '2007-08-17', '2009-06-05', '1994-09-23', '1999-10-15', '1994-07-06', '2013-07-19', '1984-11-09', '2010-07-16', '1999-03-31', '2014-11-07', '2009-12-18', '2018-09-28', '1995-09-22', '1991-02-14', '1997-12-19', '2004-06-25', '1995-11-22', '2018-12-14', '1993-06-11', '2013-09-20', '1972-03-24', '1994-10-14', '2001-11-16', '2001-12-19', '2000-05-05', '2016-12-09');

    v_movie_genre_id num_array := num_array(1, 1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 5, 5, 5, 6, 7, 7, 8, 8, 9, 9, 10, 11, 12, 12, 13, 13, 14, 15);
    v_movie_duration num_array := num_array(152, 143, 132, 113, 100, 142, 139, 142, 112, 91, 148, 136, 169, 162, 100, 127, 118, 194, 123, 81, 117, 127, 153, 175, 154, 152, 178, 155, 128);

    v_formats string_array := string_array('1080p', '4K', '720p', '3D', 'IMAX');
    v_langs string_array := string_array('EN', 'RO', 'FR', 'ES', 'RS');

    v_act_first string_array := string_array('Christian', 'Robert', 'Bruce', 'Jonah', 'Bradley', 'Tim', 'Brad', 'Tom', 'Patrick', 'Robert', 'Leonardo', 'Keanu', 'Matthew', 'Sam', 'Alex', 'Morgan', 'Anthony', 'Kate', 'Ryan', 'Tom', 'Shameik', 'Sam', 'Hugh', 'Marlon', 'John', 'Daniel', 'Elijah', 'Russell', 'Ryan');
    v_act_last string_array := string_array('Bale', 'Downey', 'Willis', 'Hill', 'Cooper', 'Robbins', 'Pitt', 'Hanks', 'Wilson', 'Englund', 'DiCaprio', 'Reeves', 'McConaughey', 'Worthington', 'Honnold', 'Freeman', 'Hopkins', 'Winslet', 'Gosling', 'Hanks', 'Moore', 'Neill', 'Jackman', 'Brando', 'Travolta', 'Radcliffe', 'Wood', 'Crowe', 'Gosling');
    v_roles string_array := string_array('Bruce Wayne', 'Tony Stark', 'John McClane', 'Seth', 'Phil', 'Andy Dufresne', 'Tyler Durden', 'Forrest Gump', 'Ed Warren', 'Freddy Krueger', 'Cobb', 'Neo', 'Cooper', 'Jake Sully', 'Himself', 'Somerset', 'Hannibal Lecter', 'Rose', 'Noah', 'Woody', 'Miles Morales', 'Dr. Grant', 'Keller Dover', 'Vito Corleone', 'Vincent Vega', 'Harry Potter', 'Frodo', 'Maximus', 'Sebastian');

BEGIN
    -- 1. Insert Genres
FOR i IN 1..15 LOOP
        INSERT INTO GENRES (id_genre, genre_name)
        VALUES (seq_genres.NEXTVAL, v_genres(i));
END LOOP;

    -- 2. Insert Movies
FOR i IN 1..29 LOOP
        INSERT INTO MOVIES (id_movie, title, description, id_genre, release_date, movie_duration, image_url)
        VALUES (seq_movies.NEXTVAL, v_movies(i), v_desc(i), v_movie_genre_id(i), TO_DATE(v_movie_date(i), 'YYYY-MM-DD'), v_movie_duration(i), '/org/example/movie-posters/' || || v_movies(i) || '.jpg');
END LOOP;

    -- 3. Insert Movie Versions
FOR i IN 1..29 LOOP
        INSERT INTO MOVIE_VERSIONS (id_version, id_movie, format, language)
        VALUES (seq_versions.NEXTVAL, i, v_formats(MOD(i, 5) + 1), v_langs(MOD(i, 5) + 1));
END LOOP;

    -- 4. Insert Actors
FOR i IN 1..29 LOOP
        INSERT INTO ACTORS (id_actor, stage_name, first_name, last_name, date_of_birth, image_url)
        VALUES (seq_actors.NEXTVAL, v_act_first(i) || ' ' || v_act_last(i), v_act_first(i), v_act_last(i), SYSDATE - 10000, '/org/example/actor-posters/' || v_act_first(i) || ' ' || v_act_last(i) || '.jpg');
END LOOP;

    -- 5. Insert Movie Cast
FOR i IN 1..29 LOOP
        INSERT INTO MOVIE_CAST (id_movie, id_actor, role)
        VALUES (i, i, v_roles(i));
END LOOP;

    -- 6. Insert Users
FOR i IN 1..25 LOOP
        INSERT INTO USERS (id_user, first_name, last_name, email, city)
        VALUES (seq_users.NEXTVAL, 'User', 'Test' || i, 'user' || i || '@test.com', 'City');
END LOOP;

    -- 7. Insert Views
FOR i IN 1..25 LOOP
        INSERT INTO VIEWS (id_view, id_user, id_version, view_date, watched_minutes)
        VALUES (seq_views.NEXTVAL, i, i, SYSDATE, 0);
END LOOP;

--     -- 8. Insert Reviews
-- FOR i IN 1..25 LOOP
--         INSERT INTO REVIEWS (id_review, id_view, rating, comment_text, predefined_option, sentiment_score)
--         VALUES (seq_reviews.NEXTVAL, i, 5, 'Great movie!', 'General', NULL);
END LOOP;

COMMIT;
END;
/