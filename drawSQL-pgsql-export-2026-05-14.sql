CREATE TABLE "users"(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255) NULL,
    "phone_number" VARCHAR(255) NULL,
    "password" VARCHAR(255) NULL,
    "status" VARCHAR(255) NULL,
    "role" VARCHAR(255) NULL
);
ALTER TABLE
    "users" ADD PRIMARY KEY("id");
ALTER TABLE
    "users" ADD CONSTRAINT "users_phone_number_unique" UNIQUE("phone_number");
CREATE TABLE "hitories"(
    "rescue_team_id" BIGINT NULL,
    "post_id" BIGINT NULL,
    "create_at" DATE NOT NULL,
    "end_at" DATE NOT NULL,
    "status" VARCHAR(255) NULL
);
CREATE TABLE "posts"(
    "id" INTEGER NOT NULL,
    "user_id" BIGINT NULL,
    "create_at" DATE NOT NULL,
    "status" VARCHAR(255) NULL,
    "content" VARCHAR(255) NOT NULL,
    "id_place" INTEGER NULL
);
ALTER TABLE
    "posts" ADD PRIMARY KEY("id");
CREATE TABLE "places"(
    "id" BIGINT NOT NULL,
    "latitude" DOUBLE PRECISION NULL,
    "longtude" DOUBLE PRECISION NULL,
    "name" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "places" ADD PRIMARY KEY("id");
CREATE TABLE "relatives"(
    "id_user" INTEGER NULL,
    "id_parent" INTEGER NULL,
    "relationship" VARCHAR(255) NULL
);
CREATE TABLE "groups"(
    "id_user" INTEGER NOT NULL,
    "rescue_id" INTEGER NULL
);
ALTER TABLE
    "groups" ADD PRIMARY KEY("id_user");
CREATE TABLE "rescue_teams"(
    "id" INTEGER NOT NULL,
    "post_id" INTEGER NULL,
    "status" VARCHAR(255) NULL,
    "id_place" INTEGER NOT NULL
);
ALTER TABLE
    "rescue_teams" ADD PRIMARY KEY("id");
CREATE TABLE "messages"(
    "id_sender" INTEGER NULL,
    "id_receiver" INTEGER NULL,
    "content" VARCHAR(255) NULL,
    "status" VARCHAR(255) NULL,
    "create_at" DATE NULL
);
CREATE TABLE "Notifications"(
    "id" BIGINT NOT NULL,
    "user_id" BIGINT NULL,
    "content" VARCHAR(255) NOT NULL,
    "create_at" DATE NOT NULL,
    "status" VARCHAR(255) NULL,
    "title" VARCHAR(255) NULL
);
ALTER TABLE
    "Notifications" ADD PRIMARY KEY("id");
ALTER TABLE
    "rescue_teams" ADD CONSTRAINT "rescue_teams_post_id_foreign" FOREIGN KEY("post_id") REFERENCES "posts"("id");
ALTER TABLE
    "rescue_teams" ADD CONSTRAINT "rescue_teams_id_place_foreign" FOREIGN KEY("id_place") REFERENCES "places"("id");
ALTER TABLE
    "hitories" ADD CONSTRAINT "hitories_post_id_foreign" FOREIGN KEY("post_id") REFERENCES "posts"("id");
ALTER TABLE
    "groups" ADD CONSTRAINT "groups_rescue_id_foreign" FOREIGN KEY("rescue_id") REFERENCES "rescue_teams"("id");
ALTER TABLE
    "hitories" ADD CONSTRAINT "hitories_rescue_team_id_foreign" FOREIGN KEY("rescue_team_id") REFERENCES "rescue_teams"("id");
ALTER TABLE
    "Notifications" ADD CONSTRAINT "notifications_id_foreign" FOREIGN KEY("id") REFERENCES "users"("id");
ALTER TABLE
    "posts" ADD CONSTRAINT "posts_id_place_foreign" FOREIGN KEY("id_place") REFERENCES "places"("id");
ALTER TABLE
    "messages" ADD CONSTRAINT "messages_id_receiver_foreign" FOREIGN KEY("id_receiver") REFERENCES "users"("id");
ALTER TABLE
    "relatives" ADD CONSTRAINT "relatives_id_user_foreign" FOREIGN KEY("id_user") REFERENCES "users"("id");
ALTER TABLE
    "relatives" ADD CONSTRAINT "relatives_id_parent_foreign" FOREIGN KEY("id_parent") REFERENCES "users"("id");
ALTER TABLE
    "messages" ADD CONSTRAINT "messages_id_sender_foreign" FOREIGN KEY("id_sender") REFERENCES "users"("id");
ALTER TABLE
    "posts" ADD CONSTRAINT "posts_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "users"("id");
ALTER TABLE
    "users" ADD CONSTRAINT "users_id_foreign" FOREIGN KEY("id") REFERENCES "groups"("id_user");