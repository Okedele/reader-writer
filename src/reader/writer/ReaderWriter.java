/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader.writer;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author user
 */
public class ReaderWriter {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Account account = new Account();
        
         for(int i = 0; i < 5; i++){
            executor.execute(new AddAPennyTask(account));
        }
        
        for(int i = 0; i < 5; i++){
            executor.execute(new RemoveAPennyTask(account));
        }
        executor.shutdown();

        while (!executor.isTerminated()) {}

        System.out.println("what is balance? " + account.getBalance());
    }
}

class AddAPennyTask implements Runnable {
    private Account account;

    public AddAPennyTask(Account account) {
        this.account = account;
    }

    public void run(){
        account.deposit(2000);
    }
}

class RemoveAPennyTask implements Runnable {
    private Account account;

    public RemoveAPennyTask(Account account) {
        this.account = account;
    }

    public void run(){
        account.withdraw(1000);
    }
}

class Account {
    private int balance = 0;
    private Lock balanceLock = new ReentrantLock();

    public int getBalance(){
        return balance;
    }

    private int getBalanceInternally() {
        balanceLock.lock();
        return balance;
    }

    private void overwriteBalance(int newBalance) {
        try {
            balance = newBalance;
        }
        finally {
            System.out.println("balance mutated to " + this.getBalance());
            balanceLock.unlock();
        }
    }

    public void deposit(int amount){
        try {
            int newBalance = amount + this.getBalanceInternally();

            // delay deliberately added to magnify the data-corruption and make it easy to see
            Thread.sleep(5);

            this.overwriteBalance(newBalance);
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void withdraw(int amount) {
        try {
            int newBalance = this.getBalanceInternally() - amount;

            // delay deliberately added to magnify the data-corruption and make it easy to see
            Thread.sleep(5);

           this.overwriteBalance(newBalance);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
