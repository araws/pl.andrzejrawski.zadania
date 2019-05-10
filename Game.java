package monsterGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static monsterGame.MoveDirection.MoveDirectionOfCharacter;

public class Game {

    private GameDifficulty chosenGameDifficulty = GameDifficulty.EASY;
    private GameBoard gameBoardLogic = new GameBoard( chosenGameDifficulty );
    private int randomFlyingCounter = chosenGameDifficulty.getNumberOfJumps();

    private List<Monster> monsterList = new ArrayList<>();
    private List<Monster> monsterToKillList = new ArrayList<>();

    private Random random = new Random();

    private char[][] gameBoard = null;

    public void start() throws IOException {
        System.out.println( "Welcome to game 'Monster'" );
//        System.out.println( "Choose the difficulty: 1 - EASY, 2 - MEDIUM, 3 - HARD" );

        Player player = new Player( 3, 6, 24 );

        BufferedReader directionReader = new BufferedReader( new InputStreamReader( System.in ) );

        monsterList.add( new Monster( 1, 6 ) );
        monsterList.add( new Monster( 3, 9 ) );
        monsterList.add( new Monster( 2, 3 ) );
        monsterList.add( new Monster( 5, 2 ) );
        monsterList.add( new Monster( 6, 8 ) );
        monsterList.add( new Monster( 8, 22 ) );
        monsterList.add( new Monster( 8, 14 ) );
        monsterList.add( new Monster( 9, 12 ) );
        monsterList.add( new Monster( 10, 12 ) );
        monsterList.add( new Monster( 11, 14 ) );

        gameBoard = new char[gameBoardLogic.getHeightWithFrame()][gameBoardLogic.getWidthWithFrame()];

        refreshGameBoard( player );

        while ((player.getLiveQuantity() > 0) && (!monsterList.isEmpty())) {
            gameLoop( player, directionReader );
        }
    }

    private void gameLoop(Player player, BufferedReader directionReader) throws IOException {

        movePlayer( player, directionReader );

        moveMonsterTowardPlayer( player );

        for (Iterator<Monster> monsterIterator = monsterList.iterator(); monsterIterator.hasNext(); ) {
            Monster monster = monsterIterator.next();
            if ((monster.getPositionX() == player.getPositionX()) && (monster.getPositionY() == player.getPositionY())) {
                player.looseLive();
                if (player.getLiveQuantity() == 0) {
                    System.err.println( "YOU'VE LOST!!!!!" );
                } else {
                    randomlySetPlayerPosition( player );
                    System.out.println( "jeszcze nie koniec" );
                }
                monster.looseLive();
                if (monster.getLiveQuantity() == 0) {
                    monsterToKillList.add( monster );
                }
            }
            for (Iterator<Monster> killingMonsterIterator = monsterList.iterator(); killingMonsterIterator.hasNext(); ) {
                Monster monsterToKill = killingMonsterIterator.next();
                if ((monster.getPositionX() == monsterToKill.getPositionX())
                        && ((monster.getPositionY() == monsterToKill.getPositionY())
                        && (!monster.equals( monsterToKill )))) {
                    monsterToKill.looseLive();
                    if (monsterToKill.getLiveQuantity() == 0) {
                        monsterToKillList.add( monsterToKill );
                    }
                    monster.looseLive();
                    if (monster.getLiveQuantity() == 0) {
                        monsterToKillList.add( monster );
                    }
                }

            }
            System.out.println( player.getLiveQuantity() );
        }
        monsterList.removeAll( monsterToKillList );

        if (monsterList.isEmpty()) {
            System.err.println( "VICTORY!!!" );
        }

        refreshGameBoard( player );
    }

    private void moveMonsterTowardPlayer(Player player) {
        for (Monster monster : monsterList) {
            int monsterPositionX = monster.getPositionX();
            int monsterPositionY = monster.getPositionY();
            int playerPositionX = player.getPositionX();
            int playerPositionY = player.getPositionY();

            if ((monsterPositionX < playerPositionX) && (monsterPositionY < playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.SE );
            } else if ((monsterPositionX < playerPositionX) && (monsterPositionY == playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.S );
            } else if ((monsterPositionX < playerPositionX) && (monsterPositionY > playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.SW );
            } else if ((monsterPositionX == playerPositionX) && (monsterPositionY > playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.W );
            } else if ((monsterPositionX > playerPositionX) && (monsterPositionY > playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.NW );
            } else if ((monsterPositionX > playerPositionX) && (monsterPositionY == playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.N );
            } else if ((monsterPositionX > playerPositionX) && (monsterPositionY < playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.NE );
            } else if ((monsterPositionX == playerPositionX) && (monsterPositionY < playerPositionY)) {
                monster.move( MoveDirectionOfCharacter.E );
            }
        }
    }

    private void movePlayer(Player player, BufferedReader directionReader) throws IOException {

        String keyBoardDirection = directionReader.readLine();

        switch (keyBoardDirection) {
            case "8":
                if (player.getPositionX() > GameBoard.getFirstColumnAfterFrame()) {
                    player.move( MoveDirectionOfCharacter.N );
                }
                break;
            case "9":
                if ((player.getPositionX() > GameBoard.getFirstColumnAfterFrame())
                        && (player.getPositionY() < gameBoardLogic.getLastColumnBeforeFrame())) {
                    player.move( MoveDirectionOfCharacter.NE );
                }
                break;
            case "6":
                if (player.getPositionY() < gameBoardLogic.getLastColumnBeforeFrame()) {
                    player.move( MoveDirectionOfCharacter.E );
                }
                break;
            case "3":
                if ((player.getPositionX() < gameBoardLogic.getLastRowBeforeFrame())
                        && (player.getPositionY() < gameBoardLogic.getLastColumnBeforeFrame())) {
                    player.move( MoveDirectionOfCharacter.SE );
                }
                break;
            case "2":
                if (player.getPositionX() < gameBoardLogic.getLastRowBeforeFrame()) {
                    player.move( MoveDirectionOfCharacter.S );
                }
                break;
            case "1":
                if ((player.getPositionX() < gameBoardLogic.getLastRowBeforeFrame())
                        && (player.getPositionY() > GameBoard.getFirstRowAfterFrame())) {
                    player.move( MoveDirectionOfCharacter.SW );
                }
                break;
            case "4":
                if (player.getPositionY() > GameBoard.getFirstRowAfterFrame()) {
                    player.move( MoveDirectionOfCharacter.W );
                }
                break;
            case "7":
                if ((player.getPositionX() > GameBoard.getFirstColumnAfterFrame())
                        && (player.getPositionY() > GameBoard.getFirstRowAfterFrame())) {
                    player.move( MoveDirectionOfCharacter.NW );
                }
                break;
            case "5":
                if (randomFlyingCounter > 0) {
                    randomlySetPlayerPosition( player );
                    randomFlyingCounter--;
                }
                break;
        }
    }

    private void refreshGameBoard(Player player) {
        for (int row = 0; row < gameBoardLogic.getHeightWithFrame(); row++) {
            for (int column = 0; column < gameBoardLogic.getWidthWithFrame(); column++) {
                if (row == 0) {
                    gameBoard[row][column] = '#';
                } else if (row == gameBoardLogic.getLastRowWithFrame()) {
                    gameBoard[row][column] = '#';
                } else if (column == 0) {
                    gameBoard[row][column] = '#';
                } else if (column == gameBoardLogic.getLastColumnWithFrame()) {
                    gameBoard[row][column] = '#';
                } else {
                    gameBoard[row][column] = ' ';
                }

                if ((row == player.getPositionX()) && (column == player.getPositionY())) {
                    gameBoard[row][column] = 'P';
                }

                for (Monster monster : monsterList) {
                    if (monster.getLiveQuantity() != 0) {
                        if ((row == monster.getPositionX()) && (column == monster.getPositionY())) {
                            gameBoard[row][column] = 'M';
                        }
                    }
                }
                System.out.print( gameBoard[row][column] );
            }
            System.out.println();

        }
        System.out.println();
    }

    public GameBoard getGameBoardLogic() {
        return gameBoardLogic;
    }

    public void randomlySetPlayerPosition(Player player) {
        player.setPositionX( random.nextInt( gameBoardLogic.getLastRowWithFrame() ) );
        player.setPositionY( random.nextInt( gameBoardLogic.getLastColumnWithFrame() ) );
    }

    public void randomlySetMonsterPosition(Monster monster) {
        monster.setPositionX( random.nextInt( gameBoardLogic.getLastRowWithFrame() ) );
        monster.setPositionY( random.nextInt( gameBoardLogic.getLastColumnWithFrame() ) );
    }
}

enum GameDifficulty {
    EASY( 25, 15, 10, 4, 3 ),
    MEDIUM( 35, 18, 20, 3, 2 ),
    HARD( 50, 23, 30, 2, 1 );

    private int boardWidth;
    private int boardHeight;
    private int numberOfMonster;
    private int numberOfPlayerLives;
    private int numberOfJumps;


    GameDifficulty(int boardWidth, int boardHeight, int numberOfMonster, int numberOfPlayerLives, int numberOfJumps) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.numberOfMonster = numberOfMonster;
        this.numberOfPlayerLives = numberOfPlayerLives;
        this.numberOfJumps = numberOfJumps;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getNumberOfMonster() {
        return numberOfMonster;
    }

    public int getNumberOfPlayerLives() {
        return numberOfPlayerLives;
    }

    public int getNumberOfJumps() {
        return numberOfJumps;
    }
}