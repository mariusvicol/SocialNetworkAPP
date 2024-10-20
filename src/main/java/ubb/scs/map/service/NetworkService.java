package ubb.scs.map.service;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.User;
import ubb.scs.map.repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class NetworkService {
    private final Repository<Long, User> userRepository;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    /**
     * @param userRepository User repository
     * @param friendshipRepository Friendship repository
     */
    public NetworkService(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        addAllFriendsLoad();
    }

    /// TODO: Find a better solution for adding all friends from file that contains friendships..
    private void addAllFriendsLoad(){
        for(Friendship friendship : friendshipRepository.findAll()){
            User user1 = userRepository.findOne(friendship.getIdUser1());
            User user2 = userRepository.findOne(friendship.getIdUser2());
            if(user1 != null && user2 != null){
                user1.addFriend(user2);
                user2.addFriend(user1);
            }
        }
    }

    /**
     * @param userId User's id
     * @return User object for user with userId id.
     */
    public User getUser(Long userId) {
        return userRepository.findOne(userId);
    }

    /**
     * @return all users from userRepository
     */
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * @param id User's id
     * @param firstName User's first name
     * @param lastName User's last name
     * @return User object for user with id id, first name firstName and last name lastName.
     */
    public User addUser(Long id, String firstName, String lastName){
        User user = new User(firstName, lastName);
        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * @param id User's id
     * revokes all friendships of user with id id
     */
    private void deleteAllFriends(Long id){
        for(Friendship friendship : friendshipRepository.findAll()){
            if(friendship.getIdUser1().equals(id)|| friendship.getIdUser2().equals(id)){
                Long idMin = min(friendship.getIdUser1(), friendship.getIdUser2());
                Long idMax = max(friendship.getIdUser1(), friendship.getIdUser2());
                removeFriendship(idMin, idMax);
            }
        }
    }

    /**
     * @param id User's id
     * @return User object for user with id id, if it exists, null otherwise.
     */
    public User removeUser(Long id){
        User user = userRepository.findOne(id);
        if(user != null){
            deleteAllFriends(id);
            return userRepository.delete(id);
        }
        return null;
    }

    /**
     * @param id User's id
     * @return list of friends of user with id id
     */
    public List<User> getFriends(Long id){
        User user = userRepository.findOne(id);
        if (user != null)
            return user.getFriends();
        else
            return null;
    }

    /**
     * @return all friendships from friendshipRepository
     */
    public Iterable<Friendship> getAllFriendships(){
        return friendshipRepository.findAll();
    }

    /**
     * @param idUser1 User1's id
     * @param idUser2 User2's id
     * @return Friendship object which was added for the friendship between user1 with id idUser1 and user2 with id idUser2.
     */
    public Friendship addFriendship(Long idUser1, Long idUser2){
        Friendship friendship = new Friendship(idUser1, idUser2);
        friendship.setId(new Tuple<>(idUser1, idUser2));
        friendship.setDate(LocalDateTime.now());
        User user1 = userRepository.findOne(idUser1);
        User user2 = userRepository.findOne(idUser2);
        if(user1 != null && user2 != null){
            user1.addFriend(user2);
            user2.addFriend(user1);
            return friendshipRepository.save(friendship);
        }
        else
            return null;
    }

    /**
     * @param idUser1 User1's id
     * @param idUser2 User2's id
     * @return Friendship object which was deleted for the friendship between user1 with id idUser1 and user2 with id idUser2, null otherwise.
     */
    public Friendship removeFriendship(Long idUser1, Long idUser2){
        Long idMin = min(idUser1, idUser2);
        Long idMax = max(idUser1, idUser2);
        Tuple<Long, Long> idTuple = new Tuple<>(idMin, idMax);
        User user1 = userRepository.findOne(idUser1);
        User user2 = userRepository.findOne(idUser2);
        if(user1 != null && user2 != null){
            user1.removeFriend(user2);
            user2.removeFriend(user1);
            return friendshipRepository.delete(idTuple);
        }
        else
            return null;
    }
}
