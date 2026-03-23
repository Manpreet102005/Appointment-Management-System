package repositories.impl;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthorisedUsersRepository {
    public final ConcurrentHashMap<String,String> authorisedUsers=new ConcurrentHashMap<>();
}
