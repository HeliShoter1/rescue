-- ============================================================
-- ENUM TYPES
-- ============================================================

CREATE TYPE "user_role" AS ENUM (
    'CITIZEN',
    'MANAGER',
    'ADMIN'
);

CREATE TYPE "user_status" AS ENUM (
    'SAFE',
    'NEED_RESCUE',
    'EMERGENCY'
);

CREATE TYPE "post_status" AS ENUM (
    'PENDING',
    'ASSIGNED',
    'IN_PROGRESS',
    'RESOLVED',
    'CANCELLED'
);

CREATE TYPE "rescue_team_status" AS ENUM (
    'AVAILABLE',
    'ON_MISSION',
    'UNAVAILABLE'
);

CREATE TYPE "history_status" AS ENUM (
    'ONGOING',
    'COMPLETED',
    'FAILED'
);

CREATE TYPE "message_status" AS ENUM (
    'SENT',
    'DELIVERED',
    'READ'
);

CREATE TYPE "notification_status" AS ENUM (
    'UNREAD',
    'READ'
);

CREATE TYPE "relationship_type" AS ENUM (
    'SPOUSE',
    'PARENT',
    'CHILD',
    'SIBLING',
    'OTHER'
);

-- ============================================================
-- TABLES
-- ============================================================

CREATE TABLE "users"(
    "id"           BIGINT NOT NULL,
    "name"         VARCHAR(255) NULL,
    "phone_number" VARCHAR(255) NULL,
    "password"     VARCHAR(255) NULL,
    "status"       "user_status" NOT NULL DEFAULT 'SAFE',
    "role"         "user_role" NOT NULL DEFAULT 'CITIZEN'
);
ALTER TABLE "users" ADD PRIMARY KEY("id");
ALTER TABLE "users" ADD CONSTRAINT "users_phone_number_unique" UNIQUE("phone_number");

CREATE TABLE "hitories"(
    "rescue_team_id" BIGINT NULL,
    "post_id"        BIGINT NULL,
    "create_at"      DATE NOT NULL,
    "end_at"         DATE NOT NULL,
    "status"         "history_status" NOT NULL DEFAULT 'ONGOING'
);

CREATE TABLE "posts"(
    "id"       INTEGER NOT NULL,
    "user_id"  BIGINT NULL,
    "create_at" DATE NOT NULL,
    "status"   "post_status" NOT NULL DEFAULT 'PENDING',
    "content"  VARCHAR(255) NOT NULL,
    "id_place" INTEGER NULL
);
ALTER TABLE "posts" ADD PRIMARY KEY("id");

CREATE TABLE "places"(
    "id"       BIGINT NOT NULL,
    "latitude" DOUBLE PRECISION NULL,
    "longtude" DOUBLE PRECISION NULL,
    "name"     VARCHAR(255) NOT NULL
);
ALTER TABLE "places" ADD PRIMARY KEY("id");

CREATE TABLE "relatives"(
    "id_user"      INTEGER NULL,
    "id_parent"    INTEGER NULL,
    "relationship" "relationship_type" NULL
);

CREATE TABLE "groups"(
    "id_user"   INTEGER NOT NULL,
    "rescue_id" INTEGER NULL
);
ALTER TABLE "groups" ADD PRIMARY KEY("id_user");

CREATE TABLE "rescue_teams"(
    "id"       INTEGER NOT NULL,
    "post_id"  INTEGER NULL,
    "status"   "rescue_team_status" NOT NULL DEFAULT 'AVAILABLE',
    "id_place" INTEGER NOT NULL
);
ALTER TABLE "rescue_teams" ADD PRIMARY KEY("id");

CREATE TABLE "messages"(
    "id_sender"   INTEGER NULL,
    "id_receiver" INTEGER NULL,
    "content"     VARCHAR(255) NULL,
    "status"      "message_status" NOT NULL DEFAULT 'SENT',
    "create_at"   DATE NULL
);

CREATE TABLE "Notifications"(
    "id"        BIGINT NOT NULL,
    "user_id"   BIGINT NULL,
    "content"   VARCHAR(255) NOT NULL,
    "create_at" DATE NOT NULL,
    "status"    "notification_status" NOT NULL DEFAULT 'UNREAD',
    "title"     VARCHAR(255) NULL
);
ALTER TABLE "Notifications" ADD PRIMARY KEY("id");

-- ============================================================
-- FOREIGN KEYS
-- ============================================================

ALTER TABLE "rescue_teams"  ADD CONSTRAINT "rescue_teams_post_id_foreign"         FOREIGN KEY("post_id")        REFERENCES "posts"("id");
ALTER TABLE "rescue_teams"  ADD CONSTRAINT "rescue_teams_id_place_foreign"         FOREIGN KEY("id_place")       REFERENCES "places"("id");
ALTER TABLE "hitories"      ADD CONSTRAINT "hitories_post_id_foreign"              FOREIGN KEY("post_id")        REFERENCES "posts"("id");
ALTER TABLE "groups"        ADD CONSTRAINT "groups_rescue_id_foreign"              FOREIGN KEY("rescue_id")      REFERENCES "rescue_teams"("id");
ALTER TABLE "hitories"      ADD CONSTRAINT "hitories_rescue_team_id_foreign"       FOREIGN KEY("rescue_team_id") REFERENCES "rescue_teams"("id");
ALTER TABLE "Notifications" ADD CONSTRAINT "notifications_id_foreign"              FOREIGN KEY("id")             REFERENCES "users"("id");
ALTER TABLE "posts"         ADD CONSTRAINT "posts_id_place_foreign"                FOREIGN KEY("id_place")       REFERENCES "places"("id");
ALTER TABLE "messages"      ADD CONSTRAINT "messages_id_receiver_foreign"          FOREIGN KEY("id_receiver")    REFERENCES "users"("id");
ALTER TABLE "relatives"     ADD CONSTRAINT "relatives_id_user_foreign"             FOREIGN KEY("id_user")        REFERENCES "users"("id");
ALTER TABLE "relatives"     ADD CONSTRAINT "relatives_id_parent_foreign"           FOREIGN KEY("id_parent")      REFERENCES "users"("id");
ALTER TABLE "messages"      ADD CONSTRAINT "messages_id_sender_foreign"            FOREIGN KEY("id_sender")      REFERENCES "users"("id");
ALTER TABLE "posts"         ADD CONSTRAINT "posts_user_id_foreign"                 FOREIGN KEY("user_id")        REFERENCES "users"("id");
ALTER TABLE "users"         ADD CONSTRAINT "users_id_foreign"                      FOREIGN KEY("id")             REFERENCES "groups"("id_user");

-- ============================================================
-- TRIGGER FUNCTIONS & TRIGGERS
-- ============================================================

-- ------------------------------------------------------------
-- TRIGGER 1: Khi posts.status -> RESOLVED hoặc CANCELLED
--   - Cập nhật hitories.status -> COMPLETED hoặc FAILED
--   - Giải phóng rescue_teams.status -> AVAILABLE
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_on_post_status_changed()
RETURNS TRIGGER AS $$
BEGIN
    -- Chỉ xử lý khi status thực sự thay đổi
    IF OLD.status = NEW.status THEN
        RETURN NEW;
    END IF;

    IF NEW.status = 'RESOLVED' THEN
        -- Cập nhật history -> COMPLETED
        UPDATE "hitories"
        SET "status" = 'COMPLETED'
        WHERE "post_id" = NEW.id
          AND "status" = 'ONGOING';

        -- Giải phóng đội cứu hộ
        UPDATE "rescue_teams"
        SET "status" = 'AVAILABLE',
            "post_id" = NULL
        WHERE "post_id" = NEW.id;

    ELSIF NEW.status = 'CANCELLED' THEN
        -- Cập nhật history -> FAILED
        UPDATE "hitories"
        SET "status" = 'FAILED'
        WHERE "post_id" = NEW.id
          AND "status" = 'ONGOING';

        -- Giải phóng đội cứu hộ
        UPDATE "rescue_teams"
        SET "status" = 'AVAILABLE',
            "post_id" = NULL
        WHERE "post_id" = NEW.id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_on_post_status_changed
AFTER UPDATE OF "status" ON "posts"
FOR EACH ROW
EXECUTE FUNCTION fn_on_post_status_changed();


-- ------------------------------------------------------------
-- TRIGGER 2: Khi INSERT post mới
--   - Gửi thông báo đến tất cả MANAGER và ADMIN
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_on_new_post()
RETURNS TRIGGER AS $$
DECLARE
    v_manager   RECORD;
    v_notif_id  BIGINT;
BEGIN
    FOR v_manager IN
        SELECT "id" FROM "users"
        WHERE "role" IN ('MANAGER', 'ADMIN')
    LOOP
        -- Sinh ID đơn giản bằng sequence (hoặc thay bằng sequence thực tế của dự án)
        v_notif_id := (
            SELECT COALESCE(MAX("id"), 0) + 1 FROM "Notifications"
        );

        INSERT INTO "Notifications"("id", "user_id", "content", "create_at", "status", "title")
        VALUES (
            v_notif_id,
            v_manager.id,
            'Có yêu cầu cứu hộ mới (Post #' || NEW.id || '): ' || LEFT(NEW.content, 100),
            CURRENT_DATE,
            'UNREAD',
            'Yêu cầu cứu hộ mới'
        );
    END LOOP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_on_new_post
AFTER INSERT ON "posts"
FOR EACH ROW
EXECUTE FUNCTION fn_on_new_post();


-- ------------------------------------------------------------
-- TRIGGER 3: Khi rescue_teams.post_id được gán (UPDATE)
--   - Cập nhật posts.status -> ASSIGNED
--   - Cập nhật rescue_teams.status -> ON_MISSION
--   - Thông báo cho người đăng bài
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_on_rescue_team_assigned()
RETURNS TRIGGER AS $$
DECLARE
    v_post_owner_id BIGINT;
    v_notif_id      BIGINT;
BEGIN
    -- Chỉ xử lý khi post_id thay đổi từ NULL -> có giá trị
    IF OLD.post_id IS NULL AND NEW.post_id IS NOT NULL THEN

        -- Cập nhật trạng thái post
        UPDATE "posts"
        SET "status" = 'ASSIGNED'
        WHERE "id" = NEW.post_id
          AND "status" = 'PENDING';

        -- Cập nhật trạng thái đội cứu hộ
        UPDATE "rescue_teams"
        SET "status" = 'ON_MISSION'
        WHERE "id" = NEW.id;

        -- Lấy user_id của người đăng bài
        SELECT "user_id" INTO v_post_owner_id
        FROM "posts"
        WHERE "id" = NEW.post_id;

        -- Gửi thông báo cho người đăng bài
        IF v_post_owner_id IS NOT NULL THEN
            v_notif_id := (
                SELECT COALESCE(MAX("id"), 0) + 1 FROM "Notifications"
            );

            INSERT INTO "Notifications"("id", "user_id", "content", "create_at", "status", "title")
            VALUES (
                v_notif_id,
                v_post_owner_id,
                'Đội cứu hộ #' || NEW.id || ' đã được phân công đến hỗ trợ bạn.',
                CURRENT_DATE,
                'UNREAD',
                'Đội cứu hộ đang trên đường'
            );
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_on_rescue_team_assigned
AFTER UPDATE OF "post_id" ON "rescue_teams"
FOR EACH ROW
EXECUTE FUNCTION fn_on_rescue_team_assigned();


-- ------------------------------------------------------------
-- TRIGGER 4: Khi users.status -> EMERGENCY
--   - Tự động tạo post cầu cứu
--   - Gửi thông báo khẩn đến MANAGER và ADMIN
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_on_user_emergency()
RETURNS TRIGGER AS $$
DECLARE
    v_manager   RECORD;
    v_post_id   INTEGER;
    v_notif_id  BIGINT;
BEGIN
    IF OLD.status = NEW.status THEN
        RETURN NEW;
    END IF;

    IF NEW.status = 'EMERGENCY' THEN
        -- Sinh post_id mới
        v_post_id := (
            SELECT COALESCE(MAX("id"), 0) + 1 FROM "posts"
        );

        -- Tự động tạo post cầu cứu
        INSERT INTO "posts"("id", "user_id", "create_at", "status", "content", "id_place")
        VALUES (
            v_post_id,
            NEW.id,
            CURRENT_DATE,
            'PENDING',
            '[KHẨN CẤP] Người dùng ' || COALESCE(NEW.name, '#' || NEW.id) || ' cần được cứu hộ ngay lập tức!',
            NULL
        );

        -- Gửi thông báo khẩn đến tất cả MANAGER và ADMIN
        FOR v_manager IN
            SELECT "id" FROM "users"
            WHERE "role" IN ('MANAGER', 'ADMIN')
        LOOP
            v_notif_id := (
                SELECT COALESCE(MAX("id"), 0) + 1 FROM "Notifications"
            );

            INSERT INTO "Notifications"("id", "user_id", "content", "create_at", "status", "title")
            VALUES (
                v_notif_id,
                v_manager.id,
                '[KHẨN CẤP] Người dùng ' || COALESCE(NEW.name, '#' || NEW.id) || ' đang trong tình trạng nguy hiểm!',
                CURRENT_DATE,
                'UNREAD',
                '🚨 Cảnh báo khẩn cấp'
            );
        END LOOP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_on_user_emergency
AFTER UPDATE OF "status" ON "users"
FOR EACH ROW
EXECUTE FUNCTION fn_on_user_emergency();


-- ------------------------------------------------------------
-- TRIGGER 5: Khi INSERT vào hitories
--   - Cập nhật rescue_teams.status -> ON_MISSION nếu chưa được set
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_on_history_insert()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE "rescue_teams"
    SET "status" = 'ON_MISSION'
    WHERE "id" = NEW.rescue_team_id
      AND "status" = 'AVAILABLE';

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_on_history_insert
AFTER INSERT ON "hitories"
FOR EACH ROW
EXECUTE FUNCTION fn_on_history_insert();