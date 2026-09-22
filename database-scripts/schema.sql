CREATE SEQUENCE seq_genres START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_movies START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_versions START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_actors START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_users START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_views START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_reviews START WITH 1 INCREMENT BY 1;


CREATE TABLE GENRES (
    id_genre INT PRIMARY KEY,
    genre_name VARCHAR2(50) NOT NULL UNIQUE
);


CREATE TABLE MOVIES (
    id_movie INT PRIMARY KEY,
    title VARCHAR2(150) NOT NULL,
    description VARCHAR2(1000) NOT NULL,
    id_genre INT,
    release_date DATE NOT NULL,
    movie_duration INT NOT NULL,
    rating_mediu NUMBER(4,2) DEFAULT 0,
    image_url VARCHAR2(255)

    CONSTRAINT fk_movie_genre
        FOREIGN KEY (id_genre)
        REFERENCES GENRES(id_genre),

    CONSTRAINT chk_rating_mediu
        CHECK (rating_mediu BETWEEN 0.00 AND 10.00),

    CONSTRAINT chk_movie_duration
        CHECK (movie_duration > 0)
);


CREATE TABLE MOVIE_VERSIONS (
    id_version INT PRIMARY KEY,
    id_movie INT NOT NULL,
    format VARCHAR2(20) NOT NULL,
    language VARCHAR2(20) NOT NULL,

    CONSTRAINT fk_version_movie
        FOREIGN KEY (id_movie)
        REFERENCES MOVIES(id_movie)
        ON DELETE CASCADE,

    CONSTRAINT uq_movie_format_lang
        UNIQUE(id_movie, format, language)
);


CREATE TABLE ACTORS (
    id_actor INT PRIMARY KEY,
    stage_name VARCHAR2(50),
    first_name VARCHAR2(50) NOT NULL,
    last_name VARCHAR2(50) NOT NULL,
    date_of_birth DATE,
    image_url VARCHAR2(255)
);


CREATE TABLE MOVIE_CAST (
    id_movie INT,
    id_actor INT,
    role VARCHAR2(50) NOT NULL,

    PRIMARY KEY (id_movie, id_actor),

    CONSTRAINT fk_cast_movie
        FOREIGN KEY (id_movie)
        REFERENCES MOVIES(id_movie)
        ON DELETE CASCADE,

    CONSTRAINT fk_cast_actor
        FOREIGN KEY (id_actor)
        REFERENCES ACTORS(id_actor)
        ON DELETE CASCADE
);


CREATE TABLE USERS (
    id_user INT PRIMARY KEY,
    first_name VARCHAR2(50) NOT NULL,
    last_name VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    city VARCHAR2(50)
);


CREATE TABLE VIEWS (
    id_view INT PRIMARY KEY,
    id_user INT NOT NULL,
    id_version INT NOT NULL,
    view_date DATE DEFAULT SYSDATE NOT NULL,
    watched_minutes INT,
    view_status VARCHAR2(20) NOT NULL,

    CONSTRAINT chk_view_minutes
        CHECK (watched_minutes >= 0),

    CONSTRAINT chk_view_status
        CHECK (view_status IN ('IN_PROGRESS', 'COMPLETED')),

    CONSTRAINT fk_view_user
        FOREIGN KEY (id_user)
        REFERENCES USERS(id_user)
        ON DELETE CASCADE,

    CONSTRAINT fk_view_version
        FOREIGN KEY (id_version)
        REFERENCES MOVIE_VERSIONS(id_version)
        ON DELETE CASCADE
);


CREATE TABLE REVIEWS (
    id_review INT PRIMARY KEY,
    id_view INT NOT NULL,
    rating NUMBER(1) NOT NULL,
    comment_text VARCHAR2(1000),
    predefined_option VARCHAR2(50),
    sentiment_score VARCHAR2(20),

    CONSTRAINT chk_review_rating
        CHECK (rating BETWEEN 1 AND 5),

    CONSTRAINT chk_review_sentiment
        CHECK (sentiment_score IN ('Positive', 'Negative', 'Neutral')),

    CONSTRAINT fk_review_view
        FOREIGN KEY (id_view)
        REFERENCES VIEWS(id_view)
        ON DELETE CASCADE
);

