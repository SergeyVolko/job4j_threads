package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

public boolean transfer(int fromId, int toId, int amount) {
        Account fromAccount = getById(fromId).orElseThrow(IllegalArgumentException::new);
        Account toAccount =  getById(toId).orElseThrow(IllegalArgumentException::new);
        if (amount <  0 || fromAccount.amount() < amount) {
            throw new IllegalArgumentException();
        }
        return update(new Account(fromId, fromAccount.amount() - amount))
                && update(new Account(toId, toAccount.amount() + amount));
    }
}
