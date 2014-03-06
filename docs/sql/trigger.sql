use xauth;

DELIMITER //

DROP TRIGGER IF EXISTS txauth_update_role;//
CREATE TRIGGER txauth_update_role AFTER UPDATE ON xhome_xauth_role
FOR EACH ROW BEGIN
    IF old.id!=new.id OR old.status!=new.status THEN
        -- 更新对应的用户角色信息
        UPDATE xhome_xauth_user_role AS user_role
            SET user_role.role=new.id,
            user_role.modifier=new.modifier,
            user_role.modified=new.modified,
            user_role.status=new.status
        WHERE user_role.role=old.id;
        -- 同步角色为空的user状态
        IF old.status!=new.status THEN
            -- 缓存用户角色表中status与新状态不同的用户ID
            INSERT INTO xhome_xauth_temp_user
                SELECT user FROM xhome_xauth_user_role WHERE status!=new.status;
            UPDATE xhome_xauth_user AS user
                SET user.modifier=new.modifier,
                    user.modified=new.modified,
                    user.status=new.status
                WHERE user.id NOT IN(SELECT * FROM xhome_xauth_temp_user);
            -- 清空缓存表
            DELETE FROM xhome_xauth_temp_user;
        END IF;
    END IF;

END;//

DROP TRIGGER IF EXISTS txauth_update_user;//
CREATE TRIGGER txauth_update_user AFTER UPDATE ON xhome_xauth_user
FOR EACH ROW BEGIN
    -- 更新用户角色信息
    IF old.id!=new.id OR old.status!=new.status THEN
        UPDATE xhome_xauth_user_role AS user_role
            SET user_role.user=new.id,
                user_role.modifier=new.modifier,
                user_role.modified=new.modified,
                user_role.status=new.status
        WHERE user_role.user=old.id;
    END IF;
END;//

DROP TRIGGER IF EXISTS txauth_delete_role;//
CREATE TRIGGER txauth_delete_role AFTER DELETE ON xhome_xauth_role
FOR EACH ROW BEGIN
    -- 删除用户角色信息
    DELETE FROM xhome_xauth_user_role WHERE role=old.id;
    -- 缓存用户角色表中剩余用户的ID
    INSERT INTO xhome_xauth_temp_user SELECT user FROM xhome_xauth_user_role;
    -- 删除无角色的用户
    DELETE FROM xhome_xauth_user WHERE id NOT IN(
        SELECT * FROM xhome_xauth_temp_user
    );
    -- 清空缓存表
    DELETE FROM xhome_xauth_temp_user;
END;//

DROP TRIGGER IF EXISTS txauth_delete_user;//
CREATE TRIGGER txauth_delete_user AFTER DELETE ON xhome_xauth_user
FOR EACH ROW BEGIN
    -- 删除用户角色信息
    DELETE FROM xhome_xauth_user_role WHERE user=old.id;
END;//

DELIMITER ;
