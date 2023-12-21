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
        if (accounts.containsKey(account.id())) {
            return false;
        }
        accounts.put(account.id(), new Account(account.id(), account.amount()));
        return true;
    }

    public synchronized boolean update(Account account) {
        if (getById(account.id()).isEmpty()) {
            return false;
        }
        accounts.put(account.id(), new Account(account.id(), account.amount()));
        return true;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        if (accounts.containsKey(id)) {
            var account = new Account(id, accounts.get(id).amount());
            return Optional.of(account);
        }
        return Optional.empty();
    }

public boolean transfer(int fromId, int toId, int amount) {
        if (amount <= 0) {
            return false;
        }
        var optionalFrom = getById(fromId);
        var optionalTo = getById(toId);
        if (optionalFrom.isEmpty() || optionalTo.isEmpty()) {
            return false;
        }
        var accountFrom = optionalFrom.get();
        var accountTo = optionalTo.get();
        if (accountFrom.amount() < amount) {
            return false;
        }
        update(new Account(accountFrom.id(), accountFrom.amount() - amount));
        update(new Account(accountTo.id(), accountTo.amount() + amount));
        return true;
    }
}
