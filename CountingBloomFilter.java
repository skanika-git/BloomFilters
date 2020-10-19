// Counting Bloom Filter
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

class CountingBloomFilter {
    static int[] counter;
    static int[] hashes;
    static int counterSize;
    static Random rand = new Random();
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter number of elements to be encoded");
        int encodeEleSize = sc.nextInt(); //1000

        System.out.println("Enter number of elements to be removed");
        int removeEleSize = sc.nextInt(); //500

        System.out.println("Enter number of elements to be encoded additionally");
        int toAddEleSize = sc.nextInt(); //500

        System.out.println("Enter size of CountingMap");
        counterSize = sc.nextInt(); //10000
        counter = new int[counterSize];

        System.out.println("Enter number of Hashes");
        int numHashes = sc.nextInt();//7

        hashes = fillHashArray(numHashes);
        HashSet<Integer> setA = generateSet(encodeEleSize);
        encode(setA);
        removeElements(setA,removeEleSize);
        HashSet<Integer> setB = generateSet(toAddEleSize);
        encode(setB);
        int elementsInSet = findCountOfSet(setA);
        System.out.print("The number of elements of SetA in filter = ");
        printValues(elementsInSet);
    }

    public static int[] fillHashArray(int numberOfHash) {
        int newRand;
        int[] hashes = new int[numberOfHash];
        HashSet<Integer> set = new HashSet<>();
        for(int i = 0; i<numberOfHash;i++)
        {
            while(true)
            {
                newRand = rand.nextInt((Integer.MAX_VALUE - 1) + 1);
                //newRand = new Random().nextInt((numberOfHash - 1) + 1) + 1;
                if(!set.contains(newRand))
                {
                    set.add(newRand);
                    break;
                }
            }
            hashes[i] = newRand;
        }
        return hashes;
    }

    public static int getIndex(int ele, int i) {
        int index = (hashes[i] ^ ele)% counterSize;
        return index;
    }

    public static HashSet<Integer> generateSet(int flowNumber)  {
        HashSet<Integer> eleSet = new HashSet<>();
        int flowCount = 0;
        while(flowCount < flowNumber) {
            int flowId = rand.nextInt((Integer.MAX_VALUE - 1) + 1) + 1;
            if (!eleSet.contains(flowId)) {
                eleSet.add(flowId);
                flowCount++;
            }
        }
        return eleSet;
    }

    public static void encode(HashSet<Integer> set)   {
        for(int ele : set) {
            for(int i = 0; i<hashes.length;i++) {
                int index = getIndex(ele,i);
                counter[index] += 1;
            }
        }
    }

    public static void removeElements(HashSet<Integer> setA, int numOfEle)  {
        int[] arr = new int[setA.size()];
        int k = 0;
        for(int ele : setA)
            arr[k++] = ele;
        HashSet<Integer> setToRemove = getRandomFrom(arr);
        for(int ele : setToRemove) {
            for(int i = 0;i<hashes.length;i++) {
                int index = getIndex(ele,i);
                counter[index]-=1;
            }
        }
    }

    public static HashSet<Integer> getRandomFrom(int[] arr) {
        HashSet<Integer> set = new HashSet<>();

        while(set.size()<=(arr.length/2)) {
            int nextRandomIndex = (rand.nextInt((Integer.MAX_VALUE - 1) + 1))%arr.length;
            set.add(arr[nextRandomIndex]);
        }
    return set;
    }

    public static int findCountOfSet(HashSet<Integer> set)  {
        int count = 0;
        for(int ele : set) {
            if(isPresentInCounter(ele)){
                count++;
            }
        }
        return count;
    }

    public static boolean isPresentInCounter(int element)   {
        boolean present = true;
        for(int i =0;i<hashes.length;i++) {
            int index = getIndex(element,i);
            if(counter[index]==0){
                present = false;
                break;
            }
        }
        return present;
    }

    public static void printValues(int count) {
        System.out.print(count);
    }

}
