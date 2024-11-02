package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements Repository<Tuple<Long, Long>, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator){
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> longLongTuple) {
        Friendship friendship = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships WHERE id_user1 = ? AND id_user2 = ?")) {
            statement.setLong(1, longLongTuple.getFirst());
            statement.setLong(2, longLongTuple.getSecond());
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    Long idUser1 = resultSet.getLong("id_user1");
                    Long idUser2 = resultSet.getLong("id_user2");
                    LocalDate date = resultSet.getDate("date").toLocalDate();
                    friendship = new Friendship(idUser1, idUser2);
                    friendship.setDate(date);
                    friendship.setId(new Tuple<>(idUser1, idUser2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(friendship);
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
        ResultSet resultSet = statement.executeQuery()) {
            while(resultSet.next()) {
                Long idUser1 = resultSet.getLong("id_user1");
                Long idUser2 = resultSet.getLong("id_user2");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                Friendship friendship = new Friendship(idUser1, idUser2);
                friendship.setDate(date);
                friendship.setId(new Tuple<>(idUser1, idUser2));
                friendships.add(friendship);
            }
            return friendships;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        int rez;
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO friendships (id_user1, id_user2, date) VALUES (?, ?, ?)")) {
            statement.setLong(1, entity.getIdUser1());
            statement.setLong(2, entity.getIdUser2());
            statement.setDate(3, Date.valueOf(LocalDate.now()));
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rez == 1 ? Optional.empty() : Optional.of(entity);
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> longLongTuple) {
        int rez;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM friendships WHERE id_user1 = ? AND id_user2 = ?")) {
            statement.setLong(1, longLongTuple.getFirst());
            statement.setLong(2, longLongTuple.getSecond());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rez == 1 ? Optional.of(new Friendship(longLongTuple.getFirst(), longLongTuple.getSecond())) : Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        validator.validate(entity);
        int rez;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE friendships SET date = ? WHERE id_user1 = ? AND id_user2 = ?")) {
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setLong(2, entity.getIdUser1());
            statement.setLong(3, entity.getIdUser2());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rez == 1 ? Optional.empty() : Optional.of(entity);
    }
}
