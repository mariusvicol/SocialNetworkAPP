package ubb.scs.map.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private final String firstName;
    private final String lastName;
    private final List<User> friends;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
    }

    @Override
    public String toString() {
        return "Utilizator ID-> " + getId() + ": " + firstName + " " + lastName;
    }

    public void setFriends(List<User> friends) {
        this.friends.clear();
        this.friends.addAll(friends);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}