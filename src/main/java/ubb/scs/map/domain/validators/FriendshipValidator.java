package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errors = "";
        if (entity.getIdUser1() == null || entity.getIdUser2() == null) {
            errors += "The ids of the users must not be null!\n";
        }
        if (entity.getIdUser1().equals(entity.getIdUser2())) {
            errors += "User cannot be friend with himself!\n";
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
