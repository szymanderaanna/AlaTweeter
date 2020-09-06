package dao.impl;

import dao.AbstractDao;
import dao.AppUserDAO;
import model.AppUser;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySQLAppUserDAO extends AbstractDao implements AppUserDAO {
    @Override
    public HashSet<AppUser> getAll() {
        TypedQuery<AppUser> query = entityManager.createQuery("select u from AppUser u", AppUser.class);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public void saveUser(AppUser user) {
        hibernateUtil.save(user);

    }

    @Override
    public AppUser getUserByEmail(String email) {
        TypedQuery<AppUser> query = entityManager.createQuery("select u from AppUser u where email = :email", AppUser.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    @Override
    public AppUser getUserByLogin(String login) {
        TypedQuery<AppUser> query = entityManager.createQuery("select u from AppUser u where login = :login", AppUser.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }

    @Override
    public HashSet<AppUser> getFollowedUsers(Long userId) {
        Query query = entityManager
                .createQuery("select followedByUser from AppUser u where u.id = :userId");
        return new HashSet<AppUser>(query.setParameter("userId", userId).getResultList());
    }

    @Override
    public HashSet<AppUser> getNotFollowedUsers(AppUser user) {
        TypedQuery<AppUser> query = entityManager.createQuery("select  u from AppUser u where u.login !=:login", AppUser.class);
        query.setParameter("login", user.getLogin());
        List<AppUser> users = new ArrayList<>(query.getResultList());
        users.removeAll(getFollowedUsers(user.getId()));
        return new HashSet<>(users);
    }

    @Override
    public HashSet<AppUser> getFollowers(Long userId) {
        Query query = entityManager
                .createQuery("select followers from AppUser  u where u.id = :userId");
        return new HashSet<>(query.setParameter("userId", userId).getResultList());


    }

    @Override
    public void follow(AppUser currentUser, AppUser userToFollow) {
        currentUser.getFollowedByUser().add(userToFollow);
        saveUser(currentUser);
    }

    @Override
    public void unfollow(AppUser currentUser, AppUser userToUnfollow) {
        currentUser.getFollowedByUser().remove(userToUnfollow);
        saveUser(currentUser);

    }

    @Override
    public void deleteUser(Long id) {
        deleteFromRelation(id);
        hibernateUtil.delete(AppUser.class, id);
    }


    private void deleteFromRelation(Long id) {
        AppUser appUser = entityManager.find(AppUser.class, id);
        Set<AppUser> followers = getFollowers(id);
        for (AppUser follower : followers) {
            follower.getFollowedByUser().remove(appUser);
            saveUser(follower);
        }
    }
}
