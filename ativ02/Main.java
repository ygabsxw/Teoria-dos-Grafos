package ativ02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;



// codigo usado da implementacao da ativ01 com as alteracoes para realizar a busca em profundidade
// fontes: https://www.ime.usp.br/~pf/algoritmos_para_grafos/aulas/dfs.html + alteracoes para deixar iterativo com pilha com ajuda do copilot



public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int option;
        String fileName = "";

        do {
            System.out.println("1 - Small graph (100 vertices)");
            System.out.println("2 - Big graph (50000 vertices)");
            System.out.println("3 - Custom graph");
            System.out.println("0 - Exit");
            System.out.print("Choose an option: ");
            option = sc.nextInt();
            switch (option) {
                case 1:
                    fileName = "ativ02/graph-test-100.txt";
                    break;
                case 2:
                    fileName = "ativ02/graph-test-50000.txt";
                    break;
                case 3: 
                    System.out.print("Write the name or path of the file: ");
                    fileName = sc.next();
                    break;
                case 0:
                    break;
                default:
                    break;
            }
            fileName = "ativ02/" + fileName; // ajustar o path
            Graph graph = readGraph(fileName);
            graph.sort(); // ordenar em ordem lexicografica

            System.out.print("Select a vertice: ");
            int v = sc.nextInt(); // vertice
            
            dfsIterativo(graph, 1, v);

        } while(option != 0);

        sc.close();
    }

    // gemini me ajudou nessa parte a fazer a leitura com bufferReader
    public static Graph readGraph(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String firstLine = reader.readLine();

            String[] parts = firstLine.trim().split("\\s+");

            int n = Integer.parseInt(parts[0]); // vértices
            int m = Integer.parseInt(parts[1]); // arestas

            Graph graph = new Graph(n);

            for (int i = 0; i < m; i++) {
                String edgeLine = reader.readLine();
                if (edgeLine == null || edgeLine.trim().isEmpty()) {
                    i--;
                    continue;
                }

                String[] edgeParts = edgeLine.trim().split("\\s+");
                int o = Integer.parseInt(edgeParts[0]); // origem
                int d = Integer.parseInt(edgeParts[1]); // destino

                graph.addEdge(o, d);
            }

            return graph;

        } catch (IOException e) { // FileNotFoundException é um tipo de IOException
            System.err.println("Error to read the file '" + fileName + "': " + e.getMessage());
            return null;
        }
    }

    public static void dfsIterativo(Graph graph, int start, int vertice) {
        int n = graph.size();
        int[] cor = new int[n + 1]; 
        int[] td = new int[n + 1];
        int[] tt = new int[n + 1];  
        int time = 0;

        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int u = stack.peek();

            if (cor[u] == 0) { 
                cor[u] = 1;
                td[u] = ++time;
            }

            boolean avancou = false;
            for (int v : graph.getSucessors(u)) {
                if (cor[v] == 0) {
                    System.out.println("Tree edge: " + u + " -> " + v);
                    stack.push(v);
                    avancou = true;
                    break;
                } else if (u == vertice) { // só classifica se for o vértice escolhido
                    if (cor[v] == 1) {
                        System.out.println("Back edge: " + u + " -> " + v);
                    } else if (td[u] < td[v]) {
                        System.out.println("Forward edge: " + u + " -> " + v);
                    } else {
                        System.out.println("Cross edge: " + u + " -> " + v);
                    }
                }
            }


            if (!avancou) {
                cor[u] = 2;
                tt[u] = ++time;
                stack.pop();
            }
        }
    }
}


class Graph {
    private int n; // num vertices
    private List<List<Integer>> sucessorList;
    private List<List<Integer>> predecessorList;

    public Graph(int n) {
        this.n = n;
        sucessorList = new ArrayList<>(n + 1);
        predecessorList = new ArrayList<>(n + 1);

        for (int i = 0; i <= n; i++) {
            sucessorList.add(new ArrayList<>());
            predecessorList.add(new ArrayList<>());
        }
    }

    public void addEdge(int o, int d) {
        sucessorList.get(o).add(d); // origem -> destino
        predecessorList.get(d).add(o); // destino -> origem
    }

    public List<Integer> getSucessors(int v) {
        return sucessorList.get(v);
    }

    public List<Integer> getPredecessors(int v) {
        return predecessorList.get(v);
    }

    public int size() {
        return n;
    }

    // deixar em ordem lexicografica
    public void sort() {
        for (int i = 1; i <= n; i++) {
            sucessorList.get(i).sort(Integer::compareTo);
        }
    }

}