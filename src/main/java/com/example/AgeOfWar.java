package com.example;
import java.util.*;

class Castle {
    String name;
    int points;
    Map<String, Object> requirements;
    boolean owned;
    Player owner;
    String group;

    public Castle(String name, int points, Map<String, Object> requirements, String group) {
        this.name = name;
        this.points = points;
        this.requirements = requirements;
        this.owned = false;
        this.owner = null;
        this.group = group;
    }
}

class Player {
    String name;
    int score;
    List<Castle> capturedCastles;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.capturedCastles = new ArrayList<>();
    }
}

public class AgeOfWar {

    static String rollDie() {
        String[] sides = {"1infantry", "2infantry", "3infantry", "archer", "cavalry", "daimyo"};
        return sides[new Random().nextInt(sides.length)];
    }

    static Map<String, List<Castle>> groupCastlesByColor(List<Castle> castles) {
        Map<String, List<Castle>> groups = new HashMap<>();
        for (Castle castle : castles) {
            groups.computeIfAbsent(castle.group, k -> new ArrayList<>()).add(castle);
        }
        return groups;
    }

    static void calculateGroupScore(List<Castle> castles, Player player) {
        Map<String, List<Castle>> groupedCastles = groupCastlesByColor(castles);
        Map<String, Integer> groupBonuses = Map.of(
            "Green", 0, "Red", 1, "Grey", 8, "Black", 4, "Purple", 8, "Yellow", 10
        );

        for (String group : groupedCastles.keySet()) {
            List<Castle> groupCastles = groupedCastles.get(group);
            if (groupCastles.stream().allMatch(c -> c.owner == player)) {
                player.score += groupBonuses.get(group);
                System.out.println(player.name + " received a group bonus for owning all " + group + " castles!");
            }
        }
    }

    static boolean handleInfantrySplit(String splitRequirement, List<Integer> infantryDice, Scanner scanner) {
        String[] groups = splitRequirement.split("\\+");
        List<Integer> requiredGroups = new ArrayList<>();
        for (String group : groups) {
            requiredGroups.add(Integer.parseInt(group));
        }

        for (int requiredGroup : requiredGroups) {
            int currentSum = 0;
            System.out.println("Distribute dice to meet the requirement: " + requiredGroup);
            while (currentSum < requiredGroup) {
                System.out.println("Available infantry dice: " + infantryDice);
                System.out.println("Enter a die value to use (or type 'cancel' to quit):");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("cancel")) {
                    System.out.println("Failed to meet the split requirement.");
                    return false;
                }

                try {
                    int dieValue = Integer.parseInt(input);
                    if (infantryDice.contains(dieValue)) {
                        currentSum += dieValue;
                        infantryDice.remove(Integer.valueOf(dieValue));
                        System.out.println("Progress: " + currentSum + "/" + requiredGroup);
                    } else {
                        System.out.println("Invalid die value or already used. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid die value.");
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize castles
        List<Castle> castles = new ArrayList<>();
        // https://www.youtube.com/watch?v=rCi9nY9MxtA
        castles.add(new Castle("Ludicolo", 3, Map.of("infantry", "4", "archer", 1, "cavalry", 1, "daimyo", 1), "Green"));
        // https://www.youtube.com/watch?v=wUnLrM04fZ4
        castles.add(new Castle("Talonflame", 2, Map.of("infantry", "4+4"), "Red"));
        // https://www.youtube.com/watch?v=r-M1c6W34xc
        castles.add(new Castle("Hawlucha", 2, Map.of("infantry", "2+3", "cavalry", 2), "Red"));
        // https://www.youtube.com/watch?v=qJetxcx0Rhw
        // castles.add(new Castle("Klinklang", 3, Map.of("infantry", "3", "archer", 1, "cavalry", 1), "Grey"));
        // https://www.youtube.com/watch?v=BYEp8Puf-as
        // castles.add(new Castle("Pangoro", 2, Map.of("infantry", "3", "archer", 1, "cavalry", 1, "daimyo", 1), "Grey"));
        // https://www.youtube.com/watch?v=DrlEuCURuYk
        // castles.add(new Castle("Machamp", 1, Map.of("archer", "2", "daimyo", 1), "Grey"));
        // https://www.youtube.com/watch?v=eHz3TFjL_3s
        // castles.add(new Castle("Gyarados", 2, Map.of("infantry", "8", "daimyo", 1), "Blue"));
        // https://www.youtube.com/watch?v=9m_c326kspw
        // castles.add(new Castle("Jellicent", 1, Map.of("cavalry", 1, "daimyo", 2), "Blue"));
        // castles.add(new Castle("Gengar", 4, Map.of("archer", 2, "cavalry", 2), "Purple"));
        // castles.add(new Castle("Naganadel", 1, Map.of("infantry", "6", "archer", 1, "cavalry", 1, "daimyo", 1), "Purple"));
        // castles.add(new Castle("Zapdos", 2, Map.of("cavalry", 1, "archer", 1, "daimyo", 1), "Yellow"));
        // castles.add(new Castle("Electivire", 2, Map.of("infantry", "7", "archer", 2), "Yellow"));
        // castles.add(new Castle("Vespiquen", 1, Map.of("infantry", "10"), "Yellow"));
        // castles.add(new Castle("Fan Rotom", 3, Map.of("infantry", "5", "archer", 1, "cavalry", 2), "Yellow"));

        // Create players
        System.out.println("Enter the number of players:");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("Enter name for Player " + i + ":");
            players.add(new Player(scanner.nextLine()));
        }

        // Game loop
        int currentPlayerIndex = 0;
        while (true) {
            // Check if all castles are occupied
            boolean allCastlesOccupied = castles.stream().allMatch(c -> c.owned);
            if (allCastlesOccupied) {
                System.out.println("All castles are occupied. Game over!");
                break;
            }

            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("\n" + currentPlayer.name + "'s turn!");

            // Roll dice
            List<String> dice = new ArrayList<>();
            for (int i = 0; i < 7; i++) dice.add(rollDie());
            List<String> keptDice = new ArrayList<>();
            boolean keepRolling = true;

            while (keepRolling && !dice.isEmpty()) {
                System.out.println("Current dice: " + dice);
                System.out.println("Kept dice so far: " + keptDice);
                System.out.println("Enter dice to keep (comma-separated, or 'none'):");
                String input = scanner.nextLine().replaceAll("\\s+", "");

                if (!input.equalsIgnoreCase("none")) {
                    List<String> newlyKeptDice = Arrays.asList(input.split(","));
                    for (String die : newlyKeptDice) {
                        if (dice.contains(die)) {
                            keptDice.add(die);
                            dice.remove(die);
                        } else {
                            System.out.println("Die " + die + " not available.");
                        }
                    }
                }

                if (!dice.isEmpty()) dice.remove(0);
                for (int i = 0; i < dice.size(); i++) dice.set(i, rollDie());
                System.out.println("Rerolled dice: " + dice);

                System.out.println("Keep rolling? (yes/no):");
                keepRolling = scanner.nextLine().equalsIgnoreCase("yes");

                if (!keepRolling) keptDice.addAll(dice);
            }

            // Print final kept dice
            System.out.println("Final kept dice: " + keptDice);
            
            // Count infantry and other faces
            List<Integer> infantryDice = new ArrayList<>();
            Map<String, Integer> otherCounts = new HashMap<>();
            for (String die : keptDice) {
                if (die.contains("infantry")) {
                    infantryDice.add(Integer.parseInt(die.replace("infantry", "")));
                } else {
                    otherCounts.put(die, otherCounts.getOrDefault(die, 0) + 1);
                }
            }

            // Attempt attack or steal repeatedly
            while (true) {
                System.out.println("Available castles:");
                for (int i = 0; i < castles.size(); i++) {
                    Castle c = castles.get(i);
                    String status = c.owned ? "Occupied by " + c.owner.name : "Unoccupied";
                    System.out.println((i + 1) + ". " + c.name + " (Points: " + c.points + ", Requirements: " + c.requirements + ") - " + status);
                }

                System.out.println("Choose a castle to attack (1-" + castles.size() + ") or type 'skip' to pass:");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("skip")) {
                    System.out.println(currentPlayer.name + " decided to skip their turn.");
                    break;
                }

                int castleIndex;
                try {
                    castleIndex = Integer.parseInt(input) - 1;
                    if (castleIndex < 0 || castleIndex >= castles.size()) throw new Exception();
                } catch (Exception e) {
                    System.out.println("Invalid input. Try again.");
                    continue;
                }

                Castle chosenCastle = castles.get(castleIndex);

                boolean meetsRequirements = true;
                for (String req : chosenCastle.requirements.keySet()) {
                    if (req.equals("infantry") && chosenCastle.requirements.get(req) instanceof String) {
                        meetsRequirements &= handleInfantrySplit((String) chosenCastle.requirements.get(req), infantryDice, scanner);
                    } else {
                        int requiredCount = (int) chosenCastle.requirements.get(req);
                        meetsRequirements &= otherCounts.getOrDefault(req, 0) >= requiredCount;
                    }
                }

                // If owned, check for daimyo requirement
                if (chosenCastle.owned) {
                    meetsRequirements &= otherCounts.getOrDefault("daimyo", 0) >= ((int) chosenCastle.requirements.getOrDefault("daimyo", 0) + 1);
                }

                if (meetsRequirements) {
                    if (chosenCastle.owned && chosenCastle.owner == currentPlayer) {
                        System.out.println("You cannot steal your own castle!");
                    } else {
                        if (chosenCastle.owned) {
                            System.out.println("You stole " + chosenCastle.name + " from " + chosenCastle.owner.name + "!");
                            chosenCastle.owner.capturedCastles.remove(chosenCastle);
                            chosenCastle.owner.score -= chosenCastle.points;
                        } else {
                            System.out.println("You conquered " + chosenCastle.name + "!");
                        }
                        chosenCastle.owned = true;
                        chosenCastle.owner = currentPlayer;
                        currentPlayer.capturedCastles.add(chosenCastle);
                        currentPlayer.score += chosenCastle.points;
                    }
                    break; // Successful attack, exit retry loop
                } else {
                    System.out.println("Attack failed. Requirements not met. Try again or type 'skip' to pass.");
                }
            }


            // End turn
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        // Calculate group bonuses
        for (Player player : players) {
            calculateGroupScore(castles, player);
        }

        // End game
        System.out.println("\nGame Over! Final scores:");
        Player winner = players.stream().max(Comparator.comparingInt(p -> p.score)).orElse(null);
        players.forEach(player -> System.out.println(player.name + ": " + player.score + " points"));
        System.out.println("Winner: " + winner.name + " with " + winner.score + " points!");
        scanner.close();
    }
}