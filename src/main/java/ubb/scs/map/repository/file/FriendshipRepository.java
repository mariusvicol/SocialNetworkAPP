package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.validators.Validator;

import java.time.LocalDate;

public class FriendshipRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship>{
    /**
     * @param validator - the validator used
     * @param filename - the file name
     */
    public FriendshipRepository(Validator<Friendship> validator, String filename) {
        super(validator, filename);
    }

    /**
     * @param line - the line to be parsed
     * @return the entity created from the line
     */
    @Override
    public Friendship createEntity(String line) {
        String[] splited = line.split(";");
        Long idUser1 = Long.parseLong(splited[0]);
        Long idUser2 = Long.parseLong(splited[1]);

//        User user1 = userRepository.findOne(idUser1);
//        User user2 = userRepository.findOne(idUser2);

        Friendship friendship = new Friendship(idUser1, idUser2);
        friendship.setDate(LocalDate.now());
        friendship.setId(new Tuple<>(idUser1, idUser2));

//        user1.addFriend(user2);
//        user2.addFriend(user1);

        return friendship;
    }

    /**
     * @param entity - the entity to be parsed
     * @return the line created from the entity
     */
    @Override
    public String saveEntity(Friendship entity) {
        return entity.getIdUser1() + ";" + entity.getIdUser2();
    }
}
