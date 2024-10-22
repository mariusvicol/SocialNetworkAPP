package ubb.scs.map.ui;
import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.service.CommunitiesService;
import ubb.scs.map.service.NetworkService;

import java.util.List;
import java.util.Scanner;

public class Console {
    private final NetworkService networkService;
    private final CommunitiesService communitiesService;
    private final Scanner scanner;
    private final boolean isFile;

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Console(NetworkService networkService, CommunitiesService communitiesService, boolean isFile) {
        this.networkService = networkService;
        this.communitiesService = communitiesService;
        this.isFile = isFile;
        scanner = new Scanner(System.in);
    }

    private void addDefault(){
        networkService.addUser("Vicol", "Marius");
        networkService.addUser("Popescu", "Ion");
        networkService.addUser("Ionescu", "Maria");
        networkService.addUser("Georgescu", "Cristina");
        networkService.addUser("Mihalescu", "Mihai");

        networkService.addFriendship(1L, 5L);
        networkService.addFriendship(2L, 4L);
        networkService.addFriendship(5L, 4L);

    }

    private void printAllUsers(){
        int number = 0;
        for(User ignored : networkService.getUsers()){
            number++;
        }
        if(number != 0) {
            System.out.println("Users LIST: ");
            for (User user : networkService.getUsers()) {
                System.out.println(user.toString());
            }
        }
        else{
            System.out.println(ANSI_RED+"List of users is empty.\n" + ANSI_RESET);
        }
    }

    private void printAllFriendships(){
        int number = 0;
        for(Friendship ignored : networkService.getAllFriendships()){
            number++;
        }
        if(number != 0) {
            System.out.println("Friendships LIST: ");
            for (Friendship friendship : networkService.getAllFriendships()) {
                System.out.println(friendship.toString());
            }
        }
        else{
            System.out.println(ANSI_RED+"List of friendships is empty.\n"+ ANSI_RESET);
        }
    }

    private void addUserUI(){
        System.out.print("Insert user's first name: ");
        String firstName = scanner.next();
        System.out.print("Insert user's last name: ");
        String lastName = scanner.next();
        try {
            User user = networkService.addUser(firstName, lastName);
            if (user != null) {
                System.out.println(ANSI_RED+"User already exists."+ ANSI_RESET);
            } else {
                System.out.println("User successfully added.");
            }
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void deleteUserUI(){
        System.out.print("Insert user's ID: ");
        try{
            Long id = scanner.nextLong();
            User user = networkService.removeUser(id);
            if (user == null) {
                System.out.println(ANSI_RED+"User not found."+ ANSI_RESET);
            }
            else{
                System.out.println(user + " was successfully deleted." );
            }
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void addFriendUI(){
        try{
            System.out.print("Insert user's ID: ");
            Long idFriend1 = scanner.nextLong();
            System.out.print("Insert his friend ID: ");
            Long idFriend2 = scanner.nextLong();
            Friendship friendship = networkService.addFriendship(idFriend1, idFriend2);
            if(friendship == null){
                System.out.println("Friendship was successfully added.");
            }
            else{
                System.out.println(ANSI_RED+"Friendship already exists or users doesn't exit."+ ANSI_RESET);
            }
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFriendUI(){
        try{
            System.out.print("Insert user's ID: ");
            Long idFriend1 = scanner.nextLong();
            System.out.print("Insert his friend ID: ");
            Long idFriend2 = scanner.nextLong();
            Friendship friendship = networkService.removeFriendship(idFriend1, idFriend2);
            if(friendship == null){
                System.out.println(ANSI_RED+"Friendship does not exist."+ ANSI_RESET);
            }
            else{
                System.out.println("Friendship was successfully deleted.");
            }
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printFriends(){
        System.out.println("Insert user's ID: ");
        try{
            Long idUser = scanner.nextLong();
            List<User> friends = networkService.getFriends(idUser);
            if(friends == null){
                System.out.println(ANSI_RED+"User not found."+ ANSI_RESET);
            }
            else{
                if(friends.isEmpty()){
                    System.out.println(ANSI_RED+"No friends found."+ ANSI_RESET);
                }
                else {
                    System.out.println("User's friends list: ");
                    for (User user : friends) {
                        System.out.println(user.toString());
                    }
                }
            }
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void printAllCommunities(){
        List<List<Long>> communities = communitiesService.communities();
        if(communities.isEmpty()){
            System.out.println("There are not comunities.");
        }
        else {
            System.out.println("Communities are: ");
            for (List<Long> community : communities) {
                for (Long userId : community) {
                    System.out.println(networkService.getUser(userId));
                }
                System.out.println();
            }
        }
    }

    private void communitiesNumberUI(){
        int comunitiesNumber = communitiesService.communitiesNumber();
        printAllCommunities();
        System.out.println();
        System.out.println("Number of communities is: " + comunitiesNumber);
    }

    private void biggestCommunityiUI(){
        List<Long> biggestCommunity = communitiesService.biggestComunity();
        if(biggestCommunity.isEmpty()){
            System.out.println("There are not comunities.");
        }
        else {
            printAllCommunities();
            System.out.println("The biggest community is: ");
            for (Long userId : biggestCommunity) {
                System.out.println(networkService.getUser(userId));
            }
        }
    }

    private void printMeniu(){
        System.out.println("Options are: ");
        System.out.println();
        System.out.println("Option print_users: Show all users");
        System.out.println("Option print_friendships: Show all friendships");
        System.out.println("Option add_user: Add one user");
        System.out.println("Option del_user: Delete one user");
        System.out.println("Option print_friends: Show all friends for an user");
        System.out.println("Option add_friend: Add one friendship");
        System.out.println("Option del_friend: Delete one friendship");
        System.out.println("Option com_number: Show number of comunity");
        System.out.println("Option com_biggest: Show the biggest comunity");
        System.out.println("Option exit: Exit");
    }

    public void menu(){
        System.out.println("--------------");
        System.out.println("Social Network");
        System.out.println("--------------");
        if(!isFile)
            addDefault();
        while(true) {
            printMeniu();
            System.out.println();
            System.out.println("Choose one option:");
            System.out.print("    >");
            String option;
            Scanner sc = new Scanner(System.in);
            option = sc.nextLine();
            option = option.toLowerCase();
            switch (option) {
                case "print_users" -> printAllUsers();
                case "print_friendships" -> printAllFriendships();
                case "add_user" -> addUserUI();
                case "del_user" -> deleteUserUI();
                case "add_friend" -> addFriendUI();
                case "del_friend" -> deleteFriendUI();
                case "print_friends" -> printFriends();
                case "com_number" -> communitiesNumberUI();
                case "com_biggest" -> biggestCommunityiUI();
                case "exit" -> System.exit(0);
                default -> System.out.println(ANSI_RED+"Option not recognized."+ ANSI_RESET);
            }
        }
    }
}
