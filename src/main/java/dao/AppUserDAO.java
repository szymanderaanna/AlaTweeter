package dao;

import model.AppUser;

import java.util.HashSet;

public interface AppUserDAO {
    HashSet<AppUser> getAll();

    void saveUser(AppUser user);

    void deleteUser(Long id);

    AppUser getUserByEmail(String email);

    AppUser getUserByLogin(String login);

    HashSet<AppUser> getFollowedUsers (Long userId);

    HashSet<AppUser> getNotFollowedUsers (AppUser user);

    HashSet<AppUser> getFollowers (Long user);

    void follow(AppUser currentUser, AppUser userToFollow);

    void unfollow(AppUser currentUser, AppUser userToUnfollow);

}
