package com.project.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPGenetic {

    private static List<Node> nodes = new ArrayList<>();
    private static final int POPULATION_SIZE = 500;
    private static final int ELITE_SIZE = 10;
    private static final int TOURNAMENT_SIZE = 10;
    private static final double MUTATION_RATE = 0.8; //recommended 0.7-0.8
    private static final int GENERATIONS = 1000;

    public static List<Node> TSPGenAlgo(List<Node> node){
        nodes=node;
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        Population population = new Population(POPULATION_SIZE, true);
        for (int i = 0; i < GENERATIONS; i++) {
            population = geneticAlgorithm.evolvePopulation(population);
        }
        return population.getFittest().getRoute();
    }

    static class GeneticAlgorithm {
        private Random random = new Random();

        public Population evolvePopulation(Population population) {
            Population newPopulation = new Population(population.getRoutes().length, false);

            for (int i = 0; i < ELITE_SIZE; i++) {
                newPopulation.getRoutes()[i] = population.getFittest();
            }

            for (int i = ELITE_SIZE; i < population.getRoutes().length; i++) {
                Route parent1 = tournamentSelection(population);
                Route parent2 = tournamentSelection(population);
                Route child = crossover(parent1, parent2);
                newPopulation.getRoutes()[i] = child;
            }

            for (int i = ELITE_SIZE; i < newPopulation.getRoutes().length; i++) {
                mutate(newPopulation.getRoutes()[i]);
            }
            return newPopulation;
        }

        private Route crossover(Route parent1, Route parent2) {
            Route child = new Route();
            int startPos = random.nextInt(parent1.routeSize());
            int endPos = random.nextInt(parent1.routeSize());

            for (int i = 0; i < child.routeSize(); i++) {
                if (startPos < endPos && i > startPos && i < endPos) {
                    child.setCity(i, parent1.getCity(i));
                } else if (startPos > endPos) {
                    if (!(i < startPos && i > endPos)) {
                        child.setCity(i, parent1.getCity(i));
                    }
                }
            }

            for (int i = 0; i < parent2.routeSize(); i++) {
                if (!child.getRoute().contains(parent2.getCity(i))) {

                    for (int j = 0; j < child.routeSize(); j++) {
                        if (child.getCity(j) == null) {
                            child.setCity(j, parent2.getCity(i));
                            break;
                        }
                    }
                }
            }
            return child;
        }

        private void mutate(Route route) {
            for (int i = 0; i < route.routeSize(); i++) {
                if (random.nextDouble() < MUTATION_RATE) {
                    int j = random.nextInt(route.routeSize());
                    route.swapCities(i, j);
                }
            }
        }

        private Route tournamentSelection(Population population) {
            Population tournamentPopulation = new Population(TOURNAMENT_SIZE, false);
            for (int i = 0; i < TOURNAMENT_SIZE; i++) {
                int randomIndex = random.nextInt(population.getRoutes().length);
                tournamentPopulation.getRoutes()[i] = population.getRoutes()[randomIndex];
            }
            return tournamentPopulation.getFittest();
        }
    }

    static class Population {
        private Route[] routes;

        public Population(int populationSize, boolean initialise) {
            routes = new Route[populationSize];
            if (initialise) {
                for (int i = 0; i < populationSize; i++) {
                    Route route = new Route();
                    route.shuffle();
                    routes[i] = route;
                }
            }
        }

        public Route[] getRoutes() {
            return routes;
        }

        public void setRoutes(Route[] routes) {
            this.routes = routes;
        }

        public Route getFittest() {
            Route fittest = routes[0];
            for (int i = 1; i < routes.length; i++) {
                if (fittest.getDistance() > routes[i].getDistance()) {
                    fittest = routes[i];
                }
            }
            return fittest;
        }
    }

    static class Route {


        private ArrayList<Node> route = new ArrayList<>();
        private double distance = 0;

        public Route() {
            for (int i = 0; i < nodes.size(); i++) {
                route.add(nodes.get(i));
            }
        }

        public Route(ArrayList<Node> route) {
            this.route = route;
        }

        public ArrayList<Node> getRoute() {
            return route;
        }

        public void setRoute(ArrayList<Node> route) {
            this.route = route;
        }

        public double getDistance() {
            if (distance == 0) {
                double routeDistance = 0;
                for (int i = 0; i < route.size(); i++) {
                    Node fromCity = route.get(i);
                    Node toCity;
                    if (i + 1 < route.size()) {
                        toCity = route.get(i + 1);
                    } else {
                        toCity = route.get(0);
                    }
                    routeDistance += Graph.calculateDistance(fromCity, toCity);
                }
                distance = routeDistance;
            }
            return distance;
        }

        public int routeSize() {
            return route.size();
        }

        public Node getCity(int index) {
            return route.get(index);
        }

        public void setCity(int index, Node city) {
            route.set(index, city);
            distance = 0;
        }

        public void shuffle() {
            Collections.shuffle(route);
        }

        public void swapCities(int i, int j) {
            Node temp = route.get(i);
            route.set(i, route.get(j));
            route.set(j, temp);
            distance = 0;
        }
    }
}