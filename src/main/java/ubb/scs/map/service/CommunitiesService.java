package ubb.scs.map.service;
import ubb.scs.map.domain.User;

import java.util.*;

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
    private List<Long> BFS(Long id, HashMap<Long, Boolean> visited){
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

    public List<List<Long>> communities(){
        HashMap<Long, Boolean> visited = new HashMap<>();
        Iterable<User> users = networkService.getUsers();
        List<List<Long>> result = new ArrayList<>();

        users.forEach(user -> visited.put(user.getId(), Boolean.FALSE));

        visited.keySet().forEach(id -> {
            if(!visited.get(id)){
                List<Long> posibleResult = BFS(id,visited);
                result.add(posibleResult);
            }
        });
        return result;
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
    public List<Long> biggestComunity(){
        HashMap<Long, Boolean> visited = new HashMap<>();
        Iterable<User> users = networkService.getUsers();
        List<Long> result = new ArrayList<>();
        users.forEach(user -> visited.put(user.getId(), Boolean.FALSE));
        for(Long id : visited.keySet()){
            if(!visited.get(id)){
                List<Long> posibleResult = BFS(id,visited);
                if(posibleResult.size() >= result.size()){
                    result = posibleResult;
                }
            }
        }
        return result;
    }
}


