package ubb.scs.map;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.validators.FriendshipValidator;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.FriendshipDBRepository;
import ubb.scs.map.repository.database.UserDBRepository;
import ubb.scs.map.repository.file.FriendshipRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.repository.memory.InMemoryRepository;
import ubb.scs.map.service.CommunitiesService;
import ubb.scs.map.service.NetworkService;
import ubb.scs.map.ui.Console;

import java.util.Scanner;

public class StartApp {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static void startApp(){
        String startOption;
        System.out.println(ANSI_GREEN + "-------| Welcome to SOCIAL NETWORK app | -------" + ANSI_RESET);
        System.out.println("Choose what type of data you want to use: file / memory / database");
        System.out.print("    >");
        Scanner scanner = new Scanner(System.in);
        startOption = scanner.next();
        startOption = startOption.toLowerCase();
        switch (startOption){
            case "file" -> startFile();
            case "memory" -> startMemory();
            case "database" -> startDataBase();
            default -> System.out.println("Invalid option");
        }
    }

    private static void startMemory(){
        Repository<Long, User> userRepository = new InMemoryRepository<>(new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipRepository = new InMemoryRepository<>(new FriendshipValidator());
        NetworkService networkService = new NetworkService(userRepository, friendshipRepository);
        CommunitiesService communitiesService = new CommunitiesService(networkService);

        Console console = new Console(networkService, communitiesService, false);
        console.menu();
    }

    private static void startFile(){
        Repository<Long,User> userFileRepository = new UtilizatorRepository(new UserValidator(), "data/utilizatori.txt");
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = new FriendshipRepository(new FriendshipValidator(), "data/friendships.txt");
        NetworkService networkFileService = new NetworkService(userFileRepository, friendshipFileRepository);

        CommunitiesService communitiesFileSevice = new CommunitiesService(networkFileService);

        Console consoleFile = new Console(networkFileService, communitiesFileSevice, true);
        consoleFile.menu();
    }

    private static void startDataBase(){
        Repository<Long, User> userRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "0806", new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipRepository = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "0806", new FriendshipValidator());
        NetworkService networkService = new NetworkService(userRepository, friendshipRepository);

        CommunitiesService communitiesService = new CommunitiesService(networkService);

        Console consoleDB = new Console(networkService, communitiesService, true);
        consoleDB.menu();
    }
}
