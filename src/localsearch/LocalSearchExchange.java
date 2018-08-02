package localsearch;

import tspproblem.TSPProblem;

public class LocalSearchExchange extends LocalSearch {
    public LocalSearchExchange(TSPProblem problem){
        super(problem);
    }
    public LocalSearchExchange(){super();}
    // Exchange operator
    @Override
    void generateNeighbor(int[] candidate, int i, int j) {
        int temp = candidate[i];
        candidate[i] = candidate[j];
        candidate[j] = temp;
    }

    // Override to improve performance
    @Override
    public int[] search(TSPProblem problem){
        int size = problem.size();
        // Generate initial permutation
        int[] currentSolution = super.generateRandomPermutation(size);
        double currentCost = problem.cost(currentSolution);
        double nextCost = currentCost;
        if(size <= 2) return currentSolution;
        // Iterate until no better neighbor available
        do{
            currentCost = nextCost;
            // Variable holding pair of jump indexes for best neighbor
            int besti, bestj = besti = 0;
            double currentCostChange = 0;
            // Iterate through all neighbors and pick the best one
            neighborloop:
            for (int i = 0; i < size - 1; i++) {
                int ileft = (i - 1 + size) % size, iright = i + 1;
                for (int j = i + 1; j < size; j++) {
                    int jleft = j - 1, jright = (j + 1) % size;
                    // 4-opt (replacing 4 edges)
                    double costChange = 0;
                    if(iright == j){
                        costChange += problem.distanceSquare(currentSolution[j],currentSolution[ileft])
                        + problem.distanceSquare(currentSolution[i],currentSolution[jright])
                        - problem.distanceSquare(currentSolution[i],currentSolution[ileft])
                        - problem.distanceSquare(currentSolution[j],currentSolution[jright]);
                    }else if(ileft == j){
                        costChange += problem.distanceSquare(currentSolution[j],currentSolution[iright])
                                + problem.distanceSquare(currentSolution[i],currentSolution[jleft])
                                - problem.distanceSquare(currentSolution[i],currentSolution[iright])
                                - problem.distanceSquare(currentSolution[j],currentSolution[jleft]);
                    }else{
                        costChange += problem.distanceSquare(currentSolution[i],currentSolution[jleft])
                                + problem.distanceSquare(currentSolution[j],currentSolution[iright])
                                + problem.distanceSquare(currentSolution[j],currentSolution[ileft])
                                + problem.distanceSquare(currentSolution[i],currentSolution[jright])
                                - problem.distanceSquare(currentSolution[i],currentSolution[ileft])
                                - problem.distanceSquare(currentSolution[j],currentSolution[jright])
                                - problem.distanceSquare(currentSolution[i],currentSolution[iright])
                                - problem.distanceSquare(currentSolution[j],currentSolution[jleft]);
                    }
                    if(costChange < currentCostChange){
                        currentCostChange = costChange;
                        besti = i; bestj = j;//break neighborloop;
                    }
                }
            }generateNeighbor(currentSolution, besti, bestj);
            nextCost = problem.cost(currentSolution);
        }while(nextCost < currentCost);
        return currentSolution;
    }
}
