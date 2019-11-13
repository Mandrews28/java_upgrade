package got;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class MemberDAOTests {
    private MemberDAO memberDAO = InMemoryMemberDAO.getInstance();
    private Collection<Member> allMembers = memberDAO.getAll();

    private static final String BARATHEON_HOUSE_NAME = "Baratheon";
    private static final String BOLTON_HOUSE_NAME = "Bolton";
    private static final String LANNISTER_HOUSE_NAME = "Lannister";
    private static final String SNOW_HOUSE_NAME = "Snow";
    private static final String STARK_HOUSE_NAME = "Stark";
    private static final String TARGARYEN_HOUSE_NAME = "Targaryen";
    private static final String TYRELL_HOUSE_NAME = "Tyrell";

    private static final List<String> HOUSE_NAME_LIST = Stream.of(BARATHEON_HOUSE_NAME, BOLTON_HOUSE_NAME,
            LANNISTER_HOUSE_NAME, SNOW_HOUSE_NAME, STARK_HOUSE_NAME, TARGARYEN_HOUSE_NAME, TYRELL_HOUSE_NAME)
            .collect(Collectors.toUnmodifiableList());

    /**
     * Find all members whose name starts with S and sort by id (natural sort)
     */
    @Test
    public void startWithS_sortByAlpha() {
        List<Member> result = allMembers.stream()
                .filter(member -> member.getName().charAt(0) == 'S')
                .sorted()
                .collect(Collectors.toList());
        assertEquals(result.get(0).getName(), "Sansa");
        assertEquals(result.get(1).getName(), "Stannis");
    }

    /**
     * Final all Starks and sort them by name
     */
    @Test
    public void starks_alphaByName() {
        List<String> result = allMembers.stream()
                .filter(member -> member.getHouseName().equals(STARK_HOUSE_NAME))
                .map(Member::getName)
                .sorted()
                .collect(Collectors.toList());

        LinkedList<String> expected = new LinkedList<>(Arrays.asList("Arya", "Bran", "Catelyn", "Eddard", "Robb", "Sansa"));
        result.forEach(member -> assertEquals(member, expected.removeFirst()));
    }

    /**
     * Find all members whose salary is less than 80K and sort by house
     */
    @Test
    public void salaryLessThan_sortByHouseName() {
        List<String> result = allMembers.stream()
                .filter(member -> member.getSalary() < 80_000)
                .sorted(Comparator.comparing(Member::getHouseName))
                .map(Member::getName)
                .collect(Collectors.toList());

        LinkedList<String> expected = new LinkedList<>(Arrays.asList("Tommen", "Tyrion", "Arya", "Sansa", "Bran", "Loras"));
        result.forEach(member -> assertEquals(member, expected.removeFirst()));
    }

    /**
     * Sort members by house name, then by name
     */
    @Test
    public void sortByHouseName_sortByNameDesc() {
        List<String> result = allMembers.stream()
                .sorted(Comparator.comparing(Member::getHouseName)
                        .thenComparing(Member::getName, Comparator.reverseOrder()))
                .map(Member::getName)
                .collect(Collectors.toList());

        LinkedList<String> expected = new LinkedList<>(Arrays.asList("Tommen", "Stannis", "Robert", "Joffrey", "Roose", "Ramsay", "Tywin", "Tyrion", "Jaime", "Cersei", "Jon", "Sansa", "Robb", "Eddard", "Catelyn", "Bran", "Arya", "Viserys", "Daenerys", "Olenna", "Margaery", "Loras"));
        result.forEach(member -> assertEquals(member, expected.removeFirst()));
    }

    /**
     * Sort the Starks by birthdate
     */
    @Test
    public void starksByDob() {
        List<String> result = allMembers.stream()
                .filter(member -> member.getHouseName().equals(STARK_HOUSE_NAME))
                .sorted(Comparator.comparing(Member::getDob))
                .map(Member::getName)
                .collect(Collectors.toList());

        LinkedList<String> expected = new LinkedList<>(Arrays.asList("Eddard", "Catelyn", "Robb", "Sansa", "Arya", "Bran"));
        result.forEach(member -> assertEquals(member, expected.removeFirst()));
    }

    /**
     * Find all Kings and sort by name in descending order
     */
    @Test
    public void kingsByNameDesc() {
        List<String> result = allMembers.stream()
                .filter(member -> member.getTitle().equals(Title.KING))
                .map(Member::getName)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        LinkedList<String> expected = new LinkedList<>(Arrays.asList("Tommen", "Stannis", "Robert", "Robb", "Jon", "Joffrey"));
        result.forEach(member -> assertEquals(member, expected.removeFirst()));
    }

    /**
     * Find the average salary
     */
    @Test
    public void averageSalary() {
        double result = allMembers.stream()
                .mapToDouble(Member::getSalary)
                .average()
                .orElse(Double.NaN);

        System.out.println(result);
    }

    /**
     * Get the names of all the Starks, sorted in natural order
     * (note _names_, not members)
     */
    @Test
    public void namesSorted() {
        List<String> result = allMembers.stream()
                .filter(member -> member.getHouseName().equals(STARK_HOUSE_NAME))
                .sorted()
                .map(Member::getName)
                .collect(Collectors.toList());

        System.out.println(result);
    }

    /**
     * Are all the salaries greater than 100K?
     */
    @Test
    public void salariesGT100k() {
        boolean result = allMembers.stream()
                .allMatch(member -> member.getSalary() > 100_000.0);

        System.out.println("Are all salaries greater than 100K: " + result);
    }

    /**
     * Are there any members of House Greyjoy?
     */
    @Test
    public void greyjoys() {
        boolean result = allMembers.stream()
                .anyMatch(member -> member.getHouseName().equals("Greyjoy"));

        System.out.println("Are there any members of House Greyjoy: " + result);
    }

    /**
     * How many Lannisters are there?
     */
    @Test
    public void howManyLannisters() {
        long result = allMembers.stream()
                .filter(member -> member.getHouseName().equals(LANNISTER_HOUSE_NAME))
                .count();

        System.out.println("How many members of House Lannister?: " + result);

    }

    /**
     * Print the names of any three Lannisters
     */
    @Test
    public void threeLannisters() {
        List<Member> result = allMembers.stream()
                .filter(member -> member.getHouseName().equals(LANNISTER_HOUSE_NAME))
                .peek(member -> System.out.println(member.getName()))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Print the names of the Lannisters as a comma-separated string
     */
    @Test
    public void lannisterNames() {
        String result = allMembers.stream()
                .filter(member -> member.getHouseName().equals(LANNISTER_HOUSE_NAME))
                .map(Member::getName)
                .collect(Collectors.joining(", "));
        System.out.println(result);
    }

    /**
     * Who has the highest salary?
     */
    @Test
    public void highestSalary() {
        String result = allMembers.stream()
                .sorted(Comparator.comparing(Member::getSalary, Comparator.reverseOrder()))
                .map(Member::getName)
                .findFirst().orElse("No members in list");
        System.out.println(result);
    }

    /**
     * Partition members into male and female
     * (note: women are LADY or QUEEN, men are everything else)
     */
    @Test
    public void menVsWomen() {
        Set<Title> womenTitles = Stream.of(Title.QUEEN, Title.LADY)
                .collect(Collectors.toUnmodifiableSet());
        List<String> women = allMembers.stream()
                .filter(member -> womenTitles.contains(member.getTitle()))
                .map(Member::getName)
                .collect(Collectors.toList());
        System.out.println(women);

        List<String> men = allMembers.stream()
                .filter(member -> !womenTitles.contains(member.getTitle()))
                .map(Member::getName)
                .collect(Collectors.toList());
        System.out.println(men);
    }

    /**
     * Group members into Houses
     */
    @Test
    public void membersByHouse() {
        System.out.println("Group members by house");
        System.out.println("=======================");
        Set<Stream<Member>> houseGroups = HOUSE_NAME_LIST.stream()
                .peek(name -> System.out.printf("%s: ", name))
                .map(name -> memberDAO.findAllByHouseName(name))
                .peek(house -> System.out.println(house.map(Member::getName).collect(Collectors.joining(", "))))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * How many members are in each house?
     * (group by house, downstream collector using count
     */
    @Test
    public void numberOfMembersByHouse() {
        System.out.println("Number of members of each house");
        System.out.println("=======================");
        Set<Stream<Member>> houseGroups = HOUSE_NAME_LIST.stream()
                .peek(name -> System.out.printf("%s: ", name))
                .map(name -> memberDAO.findAllByHouseName(name))
                .peek(house -> System.out.println(house.count()))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Get the max, min, and ave salary for each house
     */
    @Test
    public void houseStats() {
        System.out.println("House Statistics");
        System.out.println("=======================");

        System.out.println("\nMax Salaries:");
        System.out.println("--------------");
        HOUSE_NAME_LIST.stream()
                .peek(name -> System.out.printf("%s: ", name))
                .map(name -> memberDAO.findAllByHouseName(name))
                .peek(house -> System.out.println(getMaxSalary(house)))
                .collect(Collectors.toUnmodifiableSet());

        System.out.println("\nMin Salaries:");
        System.out.println("--------------");
        HOUSE_NAME_LIST.stream()
                .peek(name -> System.out.printf("%s: ", name))
                .map(name -> memberDAO.findAllByHouseName(name))
                .peek(house -> System.out.println(getMinSalary(house)))
                .collect(Collectors.toUnmodifiableSet());

        System.out.println("\nAverage Salaries:");
        System.out.println("--------------");
        HOUSE_NAME_LIST.stream()
                .peek(name -> System.out.printf("%s: ", name))
                .map(name -> memberDAO.findAllByHouseName(name))
                .peek(house -> System.out.println(getAverageSalary(house)))
                .collect(Collectors.toUnmodifiableSet());
    }

    private double getMaxSalary(Stream<Member> members) {
        return members.sorted(Comparator.comparing(Member::getSalary, Comparator.reverseOrder()))
                .map(Member::getSalary)
                .findFirst().orElse(Double.NaN);
    }

    private double getMinSalary(Stream<Member> members) {
        return members.sorted(Comparator.comparing(Member::getSalary))
                .map(Member::getSalary)
                .findFirst().orElse(Double.NaN);
    }

    private double getAverageSalary(Stream<Member> members) {
        return members.mapToDouble(Member::getSalary)
                .average().orElse(Double.NaN);
    }
}
