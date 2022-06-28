package main.model;

import java.util.*;

public class DFA {

    private final List<Transition> transitions;
    private final List<String> startStates;
    private final List<String> endStates;

    private final List<String> startNdfaStates;
    private final List<String> endNdfaStates;

    public DFA(NDFA ndfa) {
        transitions = new ArrayList<>();
        startStates = new ArrayList<>();
        endStates = new ArrayList<>();

        startNdfaStates = ndfa.getStartStates();
        endNdfaStates = ndfa.getEndStates();

//        convert2Dfa(ndfa);
    }

//    private List<String> getListFromCombination(String givenString)
//    {
//        List<String> toReturn = new ArrayList<>();
//
////        // Create a string array, containing only the numbers
////        var stringArr = givenString.Split('q');
////        Array.Sort(stringArr);
////
////        // Add the nodes to the return list
////        foreach (string s in stringArr)
////        {
////            toReturn.Add("q" + s);
////        }
////
////        // Remove the first entry due to it being empty
////        toReturn.RemoveAt(0);
//
//        return toReturn;
//    }
//
//    private void convert2Dfa(NDFA ndfa) {
//        HashMap<String, String[]> dictDfa = new HashMap<String, String[]>();
//
//        // Add the start node(s) to the dfa dictionary to begin the table
//        List<String> startNodes = ndfa.getStartStates();
//        String combination = getCombinationOfList(startNodes);
//        String[] emptyArr = { "N/A", "N/A" };
//        dictDfa.put(combination, emptyArr);
//
//        boolean continueFirstLoop = true;
//        while(continueFirstLoop)
//        {
//            continueFirstLoop = false;
//
//            for (String node : dictDfa.keySet())
//            {
//                // Check whether a transition has not been initialized fully yet
//                if(dictDfa.get(node)[0].equals("N/A"))
//                {
//                    // Prevent the loop from breaking, as new values could be added
//                    continueFirstLoop = true;
//
//                    // Get a list containing all the node-strings of the current node-collection
//                    List<String> stringsToCheck = getListFromCombination(node);
//
//                    List<String> optionsA = new ArrayList<>();
//                    List<String> optionsB = new ArrayList<>();
//
//                    // Check for each node in the collection which transitions they have, and add it to their list
//                    foreach(string s in stringsToCheck)
//                    {
//                        string optionACleaned = getCombinationOfList(givenNdfa.getNextStates(s, "a", false));
//                        string optionBCleaned = getCombinationOfList(givenNdfa.getNextStates(s, "b", false));
//
//                        optionsA.Add(optionACleaned);
//                        optionsB.Add(optionBCleaned);
//                    }
//
//                    // Append all the sorted node-strings
//                    string optionsAMerged = getCombinationOfList(optionsA);
//                    string optionsBMerged = getCombinationOfList(optionsB);
//
//                    // Set the value of the note to the possible nodes-collection
//                    dictDfa[node][0] = optionsAMerged;
//                    dictDfa[node][1] = optionsBMerged;
//
//                    // Add new node-collections to the dictionary
//                    string[] emptyArray = { "N/A", "N/A" };
//                    if(!dictDfa.ContainsKey(optionsAMerged))
//                    {
//                        dictDfa.Add(optionsAMerged, (string[])emptyArray.Clone());
//                    }
//
//                    if (!dictDfa.ContainsKey(optionsBMerged))
//                    {
//                        dictDfa.Add(optionsBMerged, (string[])emptyArray.Clone());
//                    }
//                }
//            }
//        }
//
//        // Add all the existing transitions to the private list
//        foreach (string key in dictDfa.Keys)
//        {
//            this.transitions.Add(new CustomTransition(key, dictDfa[key][0], "a"));
//            this.transitions.Add(new CustomTransition(key, dictDfa[key][1], "b"));
//
//            //Console.WriteLine("Node: " + key + " a: " + dictDfa[key][0] + " b: " + dictDfa[key][1]);
//        }
//
//        // Loop through all the current transitions
//        foreach(CustomTransition trans in this.transitions)
//        {
//            string originalNode = trans.getOrigin();
//            string destinationNode = trans.getDestination();
//
//            // Check whether the current transition contains start states
//            foreach(string startNode in this.ndfaStartStates)
//            {
//                // Check whether the original node contains a start node
//                if(originalNode.Equals(startNode))
//                {
//                    markStartState(originalNode);
//                }
//
//                // Check whether the destination node contains a start node
//                if(destinationNode.Equals(startNode))
//                {
//                    markStartState(destinationNode);
//                }
//            }
//
//            // Check whether the current transition contains end states
//            foreach(string endNode in this.ndfaEndStates)
//            {
//                // Check whether the original node contains an end state
//                if (originalNode.Contains(endNode))
//                {
//                    markEndState(originalNode);
//                }
//
//                // Check whether the destination node contains an end node
//                if (destinationNode.Contains(endNode))
//                {
//                    markEndState(destinationNode);
//                }
//            }
//        }
//
//        // Keep only the unique values within the states
//        this.startStates = this.startStates.Distinct().ToList();
//        this.endStates = this.endStates.Distinct().ToList();
//    }
}
