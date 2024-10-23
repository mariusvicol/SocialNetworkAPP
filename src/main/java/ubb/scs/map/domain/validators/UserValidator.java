package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        //TODO: implement method validate
        String errors = "";
        if (entity.getId() == null) {
            errors += "The id of the user must not be null!\n";
        }
        if (entity.getFirstName() == null || entity.getFirstName().isEmpty()) {
            errors += "The first name of the user must not be null or empty!\n";
        }
        if (entity.getLastName() == null || entity.getLastName().isEmpty()) {
            errors += "The last name of the user must not be null or empty!\n";
        }
        if(!entity.getFirstName().matches("[a-zA-Z]+") || !entity.getLastName().matches("[a-zA-Z]+")){
            errors += "The first name and last name must contain only letters!\n";
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
