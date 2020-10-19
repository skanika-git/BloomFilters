//Coded Bloom Filter
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class CodedBloomFilter {
    static HashSet<Integer>[] setArray;
    static int[][] filterArray;
    static int[] hashes;
    static int sizeOfFilter;
    static Random rand = new Random();
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of Sets");
        int numOfSets = sc.nextInt(); //7

        System.out.println("Enter size of each Set");
        int setSize = sc.nextInt();//1000;

        setArray = new HashSet[numOfSets];
        fillSetArray(setSize);

        System.out.println("Enter number of BloomFilters");
        int numOfFilters = sc.nextInt(); //3;

        System.out.println("Enter number of bits in each filter");
        sizeOfFilter = sc.nextInt(); //30000;

        filterArray = new int[numOfFilters][sizeOfFilter];

        System.out.println("Enter number of Hashes");
        int numOfHashes = sc.nextInt(); // 7;
 
        hashes = fillHashArray(numOfHashes);
        encodeAllSets();
        int totalElements = lookUpAllSetElements();
        System.out.print("Number of elements whose lookup results are correct = ");
        System.out.print(totalElements);
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

    public static void fillSetArray(int setSize){
        for(int i =0;i<setArray.length;i++) {
            setArray[i] = generateSet(setSize);
        }
    }

    public static void encodeAllSets() {
        for(int i = 0;i<setArray.length;i++) {
            int setNum = i+1;
            String binary = Integer.toBinaryString(setNum);
            for(int si = binary.length()-1;si>=0;si--) {
                int idx = binary.length()-1-si;
                if(binary.charAt(si) == '1')
                    encode(setArray[i],filterArray[filterArray.length-idx-1]);
            }
        }
    }

    public static void encode(HashSet<Integer> set,int[] counter)   {
        for(int ele : set) {
            for(int i = 0; i<hashes.length;i++) {
                int index = getIndex(ele,i);
                counter[index] = 1;
            }
        }
    }

    public static int getIndex(int ele, int i) {
        int index = (hashes[i] ^ ele)% sizeOfFilter;
        return index;
    }

    public static int lookUpAllSetElements() {
        int count = 0;
        for(int i = 0;i<setArray.length;i++) {
            int setNum = i+1;
            int binary = Integer.parseInt(Integer.toBinaryString(setNum));
            for(int ele : setArray[i]) {
                int code = constructCode(ele);
                if(code == binary)
                    count++;
            }
        }
        return count;
    }

    public static int constructCode(int ele) {
        StringBuilder sb = new StringBuilder();
        for(int i =0;i<filterArray.length;i++) {
            if(isPresentInCounter(ele,filterArray[i])) {
                sb.append("1");
            }
            else
                sb.append("0");
        }
        return Integer.parseInt(sb.toString());
    }

    public static boolean isPresentInCounter(int element,int[] counter)   {
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

}
