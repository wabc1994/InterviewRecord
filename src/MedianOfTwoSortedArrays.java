
public class MedianOfTwoSortedArrays {

    // https://www.youtube.com/watch?v=LPFhl65R7ww ,主要讲解，
    //
    public double findMedianSortedArrays(int input1[], int input2[]) {
        //if input1 length is greater than switch them so that input1 is smaller than input2.

        if (input1.length > input2.length) {
            return findMedianSortedArrays(input2, input1);
        }

        int x = input1.length;
        int y = input2.length;

        int low = 0;
        int high = x;

        while (low <= high) {
            int partitionX = (low + high)/2;  //partitionX 代表左边input1的元素个数,
            int partitionY = (x + y + 1)/2 - partitionX;//partitionY 代表右边input2的元素个数
             // (x+y+1)/2 代表
            //if partitionX is 0 it means nothing is there on left side. Use -INF for maxLeftX
            //if partitionX is length of input then there is nothing on right side. Use +INF for minRightX
            int maxLeftX = (partitionX == 0) ? Integer.MIN_VALUE : input1[partitionX - 1];
            int minRightX = (partitionX == x) ? Integer.MAX_VALUE : input1[partitionX];

            int maxLeftY = (partitionY == 0) ? Integer.MIN_VALUE : input2[partitionY - 1];
            int minRightY = (partitionY == y) ? Integer.MAX_VALUE : input2[partitionY];

            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                //We have partitioned array at correct place
                // Now get max of left elements and min of right elements to get the median in case of even length combined array size
                // or get max of left for odd length combined array size.
                if ((x + y) % 2 == 0) {
                    return ((double)Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY))/2;
                } else {
                    return (double)Math.max(maxLeftX, maxLeftY);
                }
            } else if (maxLeftX > minRightY) { //we are too far on right side for partitionX. Go on left side.
                high = partitionX - 1;
            } else { //we are too far on left side for partitionX. Go on right side.
                low = partitionX + 1;   //  分三种情况就是maxleftx>minRighy
            }
        }

        //Only we we can come here is if input arrays were not sorted. Throw in that scenario.
        return 0.0;

    }

    public static void main(String[] args) {
        int[] x = {1, 3, 8, 9, 15};
        int[] y = {7, 11, 19, 21, 18, 25};
        MedianOfTwoSortedArrays mm = new MedianOfTwoSortedArrays();
        System.out.println(mm.findMedianSortedArrays(x, y));
    }
}

