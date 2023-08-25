/**
 * This code plays a game of MineSweeper
 * @author Leonardo Gamarra
 * @version 1.0
 */

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        // Gera uma array para armazenar o mapa do jogo.
        String[][] mineSweeper = generateMap();

        // Gera uma array para armazenar as localizações já acessadas.
        int[][][] usedLocations = new int[16][16][2];

        // Gera uma array para armazenar as localizações das bandeiras.
        int[][] flags = new int[16][16];

        // Verifica se a localização é válida.
        boolean validLocation = false;

        // Verifica se o jogador morreu ou ganhou.
        boolean isDead = false;
        boolean playerWon = false;

        // Computa os acertos.
        int rightGuesses = 0;

        // Cria variáveis para a entrada do usuário
        String method = "";
        int x = -1;
        int y = -1;

        System.out.println("Bem-vindo ao Campo Minado!");
        printMap(mineSweeper, usedLocations, flags);
        do
        {
            do
            {
                System.out.println("Insira as coordenadas da localização desejada, desta forma (s (x y)) sem os parênteses. X >= 0 / X <= 15 // Y >= 0 / Y <= 15");
                System.out.print("Ou insira (f (x y)) para adicionar ou (rf (x y)) para remover uma bandeira em uma localização:");
                x = -1;
                y = -1;
                do
                {
                    method = in.next();
                    y = in.nextInt();
                    x = in.nextInt();
                    if ((x < 0 || x > 15 || y < 0 || y > 15) || (!method.equals("f") && !method.equals("s") && !method.equals("rf")))
                    {
                        System.out.println("As entradas inseridas são inválidas!");
                        System.out.println();
                        System.out.println("Insira as coordenadas da localização desejada, desta forma (s (x y)) sem os parênteses. X >= 0 / X <= 15 // Y >= 0 / Y <= 15");
                        System.out.print("Ou insira (f (x y)) para adicionar ou (rf (x y)) para remover uma bandeira em uma localização:");
                    }
                }
                while (x < 0 || x > 15 || y < 0 || y > 15);
                if (method.equals("rf"))
                {
                    if (flags[x][y] == 1)
                    {
                        flags[x][y] = 0;
                    }
                    else
                    {
                        System.out.println("Não existem bandeiras na localização!");
                    }
                }
                if (method.equals("f"))
                {
                    if (usedLocations[x][y][0] == 0)
                    {
                        flags[x][y] = 1;
                        validLocation = true;
                    }
                    else
                    {
                        validLocation = false;
                        System.out.println("A coordenada já foi utilizada!");
                    }
                }
                else if (method.equals("s"))
                {
                    if (usedLocations[x][y][0] == 0)
                    {
                        validLocation = true;
                        usedLocations[x][y][0] = 1;
                        if (flags[x][y] == 1)
                        {
                            flags[x][y] = 0;
                        }
                    }
                    else
                    {
                        validLocation = false;
                        System.out.println("A coordenada já foi utilizada!");
                    }
                }
            }
            while (!validLocation);
            if (method.equals("s"))
            {
                if (mineSweeper[x][y].equals("B"))
                {
                    isDead = true;
                }
                else if (mineSweeper[x][y].equals("0"))
                {
                    usedLocations = updateMap(mineSweeper, usedLocations, x, y);
                }
                rightGuesses = computeGuesses(usedLocations);
                if (rightGuesses == 216)
                {
                    playerWon = true;
                }
            }
            flags = checkFlags(usedLocations, flags);
            printMap(mineSweeper, usedLocations, flags);
        }
        while (!isDead && !playerWon);
        if (isDead)
        {
            System.out.println("Você atingiu uma bomba! :(");
        }
        else if (playerWon)
        {
            System.out.println("Parabéns, você venceu o jogo! :)");
        }
    }
    public static String[][] generateMap()
    {
        String[][] mineSweeper = new String[16][16];

        // Generates bombs
        int bombs = 0;
        do
        {
            int x = (int) Math.floor(Math.random() * 16);
            int y = (int) Math.floor(Math.random() * 16);
            if (mineSweeper[x][y] == null)
            {
                mineSweeper[x][y] = "B";
                bombs++;
            }
        }
        while (bombs != 40);

        // Generates numbers
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                if (mineSweeper[i][j] == null)
                {
                    mineSweeper[i][j] = checkForBombs(mineSweeper, i, j);
                }
            }
        }

        return mineSweeper;
    }
    public static String checkForBombs(String[][] map, int x, int y)
    {
        int bombs = 0;
        for (int i = x - 1; i <= x + 1; i++)
        {
            for (int j = y - 1; j <= y + 1; j++)
            {
                if (i >= 0 && i < 16 && j >= 0 && j < 16)
                {
                    if (map[i][j] != null && map[i][j].equals("B"))
                    {
                        bombs++;
                    }
                }
            }
        }
        return Integer.toString(bombs);
    }
    public static void printMap(String[][] map, int[][][] usedLocations, int[][] flags)
    {
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < 16; i++)
        {
            System.out.print(i);
            if (i < 10)
                System.out.print("   ");
            else if (i < 15)
                System.out.print("  ");
            else
                System.out.println();
        }
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                if (j == 0)
                {
                    if (i < 10)
                        System.out.print(i + "  ");
                    else
                        System.out.print(i + " ");
                }


                if (usedLocations[i][j][0] == 1)
                {
                    System.out.print(checkColor(map[i][j]));
                }
                else if (flags[i][j] == 1)
                {
                    System.out.print("\u001B[35mF\u001B[0m");
                }
                else
                {
                    System.out.print("?");
                }
                if (j < 15)
                {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public static int[][][] updateMap(String[][] map, int[][][] usedLocations, int x, int y)
    {
        for (int i = x - 1; i <= x + 1; i++)
        {
            for (int j = y - 1; j <= y + 1; j++)
            {
                if (i >= 0 && i < 16 && j >= 0 && j < 16)
                {
                    usedLocations[i][j][0] = 1;
                }
            }
        }

        usedLocations[x][y][1] = 1;
        for (int i = x - 1; i <= x + 1; i++)
        {
            for (int j = y - 1; j <= y + 1; j++)
            {
                if ((i >= 0 && i < 16 && j >= 0 && j < 16) && map[i][j].equals("0") && usedLocations[i][j][1] == 0)
                {
                    usedLocations = updateMap(map, usedLocations, i, j);
                }
            }
        }

        return usedLocations;
    }
    public static String checkColor(String n)
    {
        if (n.equals("B"))
        {
            n = "\u001B[31;1m" + n + "\u001B[0m";
        }
        else if (n.equals("0"))
        {
            n = "\u001B[36m" + n + "\u001B[0m";
        }
        else if (n.equals("1"))
        {
            n = "\u001B[34m" + n + "\u001B[0m";
        }
        else if (n.equals("2"))
        {
            n = "\u001B[32m" + n + "\u001B[0m";
        }
        else if (n.equals("3"))
        {
            n = "\u001B[33m" + n + "\u001B[0m";
        }
        else if (n.equals("4"))
        {
            n = "\u001B[38;2;255;165;0m" + n + "\u001B[0m";
        }
        else if (n.equals("5"))
        {
            n = "\u001B[31m" + n + "\u001B[0m";
        }
        else if (n.equals("6"))
        {
            n = "\u001B[35m" + n + "\u001B[0m";
        }
        else if (n.equals("7"))
        {
            n = "\u001B[35m" + n + "\u001B[0m";
        }
        else if (n.equals("8"))
        {
            n = "\u001B[35m" + n + "\u001B[0m";
        }
        return n;
    }
    public static int computeGuesses(int[][][] usedLocations)
    {
        int counter = 0;
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                if (usedLocations[i][j][0] == 1)
                    counter++;
            }
        }
        return counter;
    }
    public static int[][] checkFlags(int[][][] usedLocations, int[][] flags)
    {
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                if (usedLocations[i][j][0] == 1)
                {
                    flags[i][j] = 0;
                }
            }
        }
        return flags;
    }
}