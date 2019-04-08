/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader.writer;
import java.util.concurrent.*;
/**
 *
 * @author user
 */
public class ReaderWriter {
    private static Account account = new Account();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        
        //create and launch 100 threads
        for(int i = 0; i < 5; i++){
            executor.execute(new AddAPennyTask());
        }
        
        for(int i = 0; i < 5; i++){
            executor.execute(new RemoveAPennyTask());
        }
        executor.shutdown();
        
        //wait until all tasks are finished
        while(!executor.isTerminated()){
        }
        
        System.out.println("what is balance? " + account.getBalance());
    }
    
    private static class AddAPennyTask implements Runnable {
        public void run(){
            account.deposit(2000);
        }
    }
    
    private static class RemoveAPennyTask implements Runnable {
        public void run(){
            account.withdraw(1000);
        }
    }
    
    private static class Account {
        private int balance = 0;
        
        public int getBalance(){
            return balance;
        }
        
        public void deposit(int amount){
            int newBalance = amount + balance;
            
            //delay deliberately added to magnify the data-corruption and make it easy to see
            try{
                Thread.sleep(5);
            }
            catch (InterruptedException ex){
            }
            
            balance = newBalance;
        }
        
        public void withdraw(int amount){
            int newBalance = balance - amount;
            
            //delay deliberately added to magnify the data-corruption and make it easy to see
            try{
                Thread.sleep(5);
            }
            catch (InterruptedException ex){
            }
            
            balance = newBalance;
        }
    }
}
