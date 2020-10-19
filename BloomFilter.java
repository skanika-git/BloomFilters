// BloomFilter
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

class BloomFilter {
    static int[] bitMap;
    static int[] hashes;
    static int bitMapSize;
    static Random rand = new Random();
    public static void main(String[] args) {
	
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter size of Set A");
        int setASize = sc.nextInt(); //1000

        System.out.println("Enter size of BitMap");
        bitMapSize = sc.nextInt(); //10000

        System.out.println("Enter number of Hashes");
        int numHashes = sc.nextInt(); //7

        int setBSize = 1000;
        bitMap = new int[bitMapSize];
        hashes = fillHashArray(numHashes);
        HashSet<Integer> setA = generateSet(setASize);
        int countA = 0;
        encodeElementsOf(setA);
        countA = lookUpElementsOf(setA);
        HashSet<Integer> setB = generateSet(setBSize);
        int countB = lookUpElementsOf(setB);
        System.out.print("The number of elements of SetA in filter = ");
        printValues(countA);
        System.out.println();
        System.out.print("The number of elements of SetB in filter = ");
        printValues(countB);

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

    public static void encodeElementsOf(HashSet<Integer> set)  {
        for(int ele : set){
            encode(ele);
        }
    }

    public static int lookUpElementsOf(HashSet<Integer> set)    {
        int count = 0;
        for(int ele : set)
        {
            if(isLookUp(ele))
                count++;
        }
        return count;
    }

    public static void encode(int ele)   {
        if(!isLookUp(ele)) {
            for(int i = 0; i<hashes.length;i++) {
                int index = getIndex(ele,i);
                bitMap[index] = 1;
            }
        }
    }

    public static int getIndex(int ele, int i) {
        int index = (hashes[i] ^ ele)% bitMapSize;
        return index;
    }

    public static boolean isLookUp(int ele) {
        boolean lookUp = true;
        for(int i = 0; i<hashes.length;i++)
        {
            int index = getIndex(ele,i);
            if(bitMap[index] == 0)
                lookUp = false;
        }
        return  lookUp;
    }

    public static void printValues(int count) {
        System.out.print(count);
    }

}
