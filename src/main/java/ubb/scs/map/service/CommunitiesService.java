package ubb.scs.map.service;
import ubb.scs.map.domain.User;

import java.util.*;
import java.util.stream.Collectors;

public class CommunitiesService {
    NetworkService networkService;

    /**
     * @param networkService - the network service
     */
    public CommunitiesService(NetworkService networkService) {
        this.networkService = networkService;
    }

    /**
     * @param id - the id of the user
     * @param visited - a map with the visited users
     * @return a list with the users from the comunity
     */
    private List<Long> BFS(Long id, Map<Long, Boolean> visited){
        Queue<Long> queue = new LinkedList<>();
        List<Long> result = new ArrayList<>();
        queue.add(id);
        visited.put(id, true);

        while(!queue.isEmpty()){
            Long node = queue.poll();
            result.add(node);
            List<User> prieteni = networkService.getFriends(node);
            for(User u : prieteni){
                if(!visited.get(u.getId())){
                    visited.put(u.getId(), Boolean.TRUE);
                    queue.add(u.getId());
                }
            }
        }
        System.out.println();
        return result;
    }

    public List<List<Long>> communities() {
        Map<Long, Boolean> visited = new HashMap<>();
        Iterable<User> users = networkService.getUsers();

        users.forEach(user -> visited.put(user.getId(), false));

        return visited.keySet().stream()
                .filter(id -> !visited.get(id))
                .map(id -> BFS(id, visited))
                .collect(Collectors.toList());
    }

    /**
     * @return the number of comunities
     */
    public int communitiesNumber(){
        int number = 0;
        HashMap<Long, Boolean> visited = new HashMap<>();
        Iterable<User> users = networkService.getUsers();
        users.forEach(user -> visited.put(user.getId(), Boolean.FALSE));
        for(Long id : visited.keySet()){
            if(!visited.get(id)){
                BFS(id, visited);
                number++;
            }
        }
        return number;
    }

    /**
     * @return the biggest comunity
     */
    public List<Long> biggestComunity() {
        Map<Long, Boolean> visited = new HashMap<>();
        Iterable<User> users = networkService.getUsers();
        List<Long> result = new ArrayList<>();

        users.forEach(user -> visited.put(user.getId(), false));

        return visited.keySet().stream()
                .filter(id -> !visited.get(id))
                .map(id -> BFS(id, visited))
                .max(Comparator.comparingInt(List::size))
                .orElse(result);
    }
}


