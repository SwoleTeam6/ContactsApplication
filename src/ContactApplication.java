import util.Input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ContactApplication {
    private static final String contactsFile = "src/contacts.txt";

    // create file and contacts.txt

    public static void createFile(String filePath, List<String> contents){
        createFile(filePath, contents, false);
    }

    public static void createFile(String filePath, List<String> contents, boolean append){
        StandardOpenOption option = append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING;
        Path path = Paths.get(filePath);
        if (!Files.exists(path)){
            try {
                Files.createDirectories(path);
                Files.createFile(path);
            }catch (IOException iox){
                iox.printStackTrace();
            }
        }

        try{
            Files.write(path, contents, option);
        } catch (IOException iox){
            iox.printStackTrace();
        }
    }

    // display from file

    public static List<String> displayOutput (String filePath){
        Path path = Paths.get(filePath);
        try{
            return Files.readAllLines(path);
        }catch(IOException iox){
            iox.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Contact splitLine(String line){
        String[] parts = line.split("\\|");
        return new Contact(parts[0], parts[1]);
    }

    public static String toLine(Contact contract){
        return String.format("%s|%s", contract.getName(), contract.getNumber());
    }

    public static void writeToFile(List<Contact> contacts){
        List<String> lines = new ArrayList<>();
        for (Contact contact : contacts){
            lines.add(toLine(contact));
        }
        createFile(contactsFile, lines);
    }

    public static List<Contact> all(){
        List<String> lines = displayOutput(contactsFile);
        List<Contact> contacts = new ArrayList<>();
        for (String line : lines){
            contacts.add(splitLine(line));
        }
        return contacts;
    }

    public static void viewAllContacts(){
        showContacts(all());
    }
    public static List<Contact> search (String search){
        List<Contact> searchResults = new ArrayList<>();
        for (Contact contact : all()){
            boolean isMatch = contact.getName().toLowerCase().contains(search.toLowerCase());
            if(isMatch){
                searchResults.add(contact);
            }
        }
        return searchResults;
    }

    public static void add(Contact contact){
        List<Contact> contacts = all();
        contacts.add(contact);
        writeToFile(contacts);
    }

    public static void addContact(){
        String enterName = Input.getString("Firstname Lastname");
        String enterNumber = Input.getString("XXX-XXX-XXXX");
        add(new Contact(enterName, enterNumber));
    }

    public static void remove(Contact contact){
        List<Contact> contacts = all();
        contacts.remove(contact);
        writeToFile(contacts);
    }

    public static void removeContact(){
        String userEnter = Input.getString("Name of contact to be remove?");
        List<Contact> results = search(userEnter);
        for(Contact contact : results){
            remove(contact);
        }
    }

    public static void showContacts(List<Contact> contacts){
        String printFormat = "| %-10s | %10s |\n";
        if(contacts.isEmpty()) {
            System.out.println("No Contacts Found.");
            return;
        }
        System.out.printf(printFormat, "Name", "Phone number");
        System.out.println("-----------------------------");
        for(Contact contact : contacts){
            System.out.printf(printFormat, contact.getName(), contact.getNumber());
        }

    }
    public static void searchContacts(){
        String userEnter = Input.getString("Search name?");
        List<Contact> results = search((userEnter));
        showContacts(results);
    }

    public static void main(String[] args) {

        System.out.println("""
                1. View contacts.
                2. Add a new contact.
                3. Search a contact by name.
                4. Delete an existing contact.
                5. Exit.""");
        int userEnter = Input.getInt();
        if(userEnter == 1){
            viewAllContacts();
        } else if (userEnter == 2) {
            addContact();
            System.out.println("Contact added!");
        } else if (userEnter == 3) {
            searchContacts();
        } else if (userEnter == 4) {
            removeContact();
            System.out.println("Contact removed!");
        } else {
            System.out.println("Good Bye");
        }

    }


}
