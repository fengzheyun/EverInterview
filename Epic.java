package Test_EA;

import java.util.*;

public class Epic {
	public static Epic pointer = new Epic(); 
	
	public static void main(String[] args){
//		int isColorfulNumber = 1234;
//		System.out.println( pointer.isFakeColorfulNumber(isColorfulNumber) );
		
//		List<Rect3Points> lists = pointer.rectangle( pointer.new Pair(0,0), 50 );
//		for( Rect3Points x:lists)
//			x.print();
		
		char[][] matrix = {	"EGCHI".toCharArray(),
							"CDBFA".toCharArray(),
							"BCABC".toCharArray(),
							"ABCHN".toCharArray(),
							"CKDMG".toCharArray()};
		List<List<Pair>> lists = pointer.wordSearch_vhd(matrix, "CABC");
		for(List<Pair> ps:lists){
			for( Pair p:ps ){
				p.print();
			}
			System.out.println();
		}
	}

	/**
	 * If given a number, find a number if it is colorful. A number is said to be colorful 
	 * if all its possible unique permutations multiplication results (except single digit 
	 * and all digits) different. Eg: if n = 1234 then permutations are (1,2),(1,3),(1,4), 
	 * (2,3),(2,4),(3,4),(1,2,3), (1,2,4), (2,3,4). That's it, no other combination. 
	 * Find the multiplication of digits in each combination and if any of them repeats then 
	 * number is not colorful.
	 * */
	public boolean isFakeColorfulNumber( int n){
		if( n<100 )
			return true;
		
		int[] digits = new int[1+(int)Math.log10(n)];
		int k = 0;
		while( n>0 ){
			digits[k++] = n%10;
			n /= 10;
		}
		
		Set<Integer> st = new HashSet<Integer>();
		for(int i=1;i<Math.pow(2, k)-1;i++){
			if( (i&(i-1))>0 ){
				int product = 1;
				int bits = i, j = 0;
				while(bits>0){
					if( 1==(bits&1) )
						product *= digits[j];
					bits >>= 1;
					j++;
				}
				if( !st.add(product) )
					return false;
			}			
		}
		return true;
	}

	/**
	 * Write a function which takes an input for a double dimensional matrix.
	 * Each page is 1000 pixels wide and 1000 pixels high.A black pixel is represented by 1 and a white pixel by 0.
	 * Return an answer set of array of row numbers for appropriate page breaks.
	 * A page break would ideally be a row with all white pixels.If the page break is encountered more than 1000 rows from 
	 * the last break then the page break should be forcefully taken 1000 rows from the last break.
	 * */
	private static final int NPIX = 1000;
	public List<Integer> pageBreaks(boolean[][] matrix){
		List<Integer> res = new LinkedList<Integer>();
		if( null==matrix||0==matrix.length||null==matrix[0]||0==matrix[0].length)
			return res;
		
		int nrow = 0, ncol = 0;
		boolean pagebreak = false;
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[i].length;j++){
				pagebreak = (pagebreak || matrix[i][j]);					
				ncol++;
				if( ncol== NPIX ){
					ncol = 0;
					nrow++;
					if( !pagebreak || NPIX==nrow ){
						res.add( nrow-1 );
						nrow = 0;
					}
				}
			}			
		}		
		return res;
	}

	/**
	 * Given a 2 dimensional point of a rectangle and its area, find permutations of all the other 
	 * 3 points of the rectangle in 2-D space. 
	 * Ex:- Given X=(0,0) and A=1 
	 * (0,1),(1,0),(1,1) 
	 * (0,-1),(-1,0),(-1,-1) 
	 * */
	public List<Rect3Points> rectangle( Pair p, int area){
		List<Rect3Points> res = new LinkedList<Rect3Points>();
		int sqrtA = (int)Math.sqrt( area );
		for(int i=1;i<=sqrtA;i++){
			if( area%i==0){
				int j = area/i;
				List<Pair> p1s = pointsWithEqualDistanceToX( p, i);
				List<Pair> p2s = i==j?refinePerpendiculars( p1s, p ):pointsWithEqualDistanceToX( p, j);				
				for( Pair t1:p1s){
					for( Pair t2:p2s){
						if( !t1.equals(t2) && p.isPerpendicular(t1, t2)){
							Pair last = find4thPoint(p, t1, t2, i, j);
							res.add( new Rect3Points(t1,t2,last) );
						}
					}
				}				
			}			
		}		
		return res;
	}
	
	private List<Pair> refinePerpendiculars( List<Pair> p1s, Pair p ){
		List<Pair> res = new LinkedList<Pair>();
		int i = 0;
		while( i<p1s.size() ){
			Pair pi = p1s.get(i++);
			int j = i;
			while( j<p1s.size() ){
				Pair pj = p1s.get(j);
				if( p.isPerpendicular(pi, pj)){
					res.add( pj );
					p1s.remove(j);
				}else
					j++;	
			}
		}
		return res;
	}
	
	
	private Pair find4thPoint(Pair p, Pair t1, Pair t2, int i, int j){
		List<Pair> p1s = pointsWithEqualDistanceToX( t1, j);
		List<Pair> p2s = pointsWithEqualDistanceToX( t2, i);
		int distL = p.dist2(t1)+p.dist2(t2);
		for(Pair p1:p1s){
			for( Pair p2:p2s){
				if( p1.equals( p2 ) && distL==p.dist2(p1)){
					return p1;					
				}				
			}
		}
		return null;
	}
	
	private List<Pair> pointsWithEqualDistanceToX( Pair X, int dist){
		List<Pair> res = new LinkedList<Pair>();
		res.add( new Pair(X.x, X.y-dist) );
		res.add( new Pair(X.x, X.y+dist) );
		res.add( new Pair(X.x-dist, X.y) );
		res.add( new Pair(X.x+dist, X.y) );
		for(int i=1;i<dist;i++){
			double val = Math.sqrt( dist*dist-i*i);
			int j = (int)val;
			if( j == Math.ceil(val) ){
				res.add( new Pair(X.x-i, X.y-j) );
				res.add( new Pair(X.x+i, X.y+j) );
				res.add( new Pair(X.x-i, X.y+j) );
				res.add( new Pair(X.x+i, X.y-j) );				
			}
		}		
		return res;
	}
	
	public class Rect3Points{
		Pair p1;
		Pair p2;
		Pair p3;
		
		public Rect3Points( Pair x, Pair y, Pair z){
			p1 = x;
			p2 = y;
			p3 = z;
		}
		
		public void print(){
			p1.print();
			p2.print();
			p3.print();
			System.out.println();
		}
	}
	
	public class Pair{
		int x;
		int y;
		public Pair(){}
		public Pair(int _x, int _y){
			x = _x;
			y = _y;
		}
		
		public void print(){
			System.out.print("("+x+","+y+")");
		}
		
		public boolean equals( Pair t){
			return ( x==t.x && y==t.y );
		}
		
		public boolean isPerpendicular( Pair t1, Pair t2){
			return 0==(t2.y-y)*(t1.y-y)+(t2.x-x)*(t1.x-x);			
		}
		
		public int dist2( Pair t){
			return (t.x-x)*(t.x-x)+(t.y-y)*(t.y-y);
		}
	}
	
	/**
	 * Write a program for a word search. If there is an NxN grid with one letter in each cell. 
	 * Let the user enter a word and the letters of the word are said to be found in the grid 
	 * either the letters match vertically, horizontally or diagonally in the grid. 
	 * 
	 * If the word is found, print the coordinates of the letters as output.
	 * */
	public List<List<Pair>> wordSearch_vhd(char[][] matrix, String word){
		List<List<Pair>> res = new LinkedList<List<Pair>>();
		if( 0==word.length() )
			return res;
		
		int[][] offset = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,-1},{-1,1}};
		int lw = word.length();
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[i].length;j++){
				if( matrix[i][j]==word.charAt(0) ){
					for(int p=0;p<offset.length;p++){
						int x = i+(lw-1)*offset[p][0], y = j+(lw-1)*offset[p][1];
						if(x>=0 && x<matrix.length && y>=0 && y<matrix[i].length){
							List<Pair> list = new LinkedList<Pair>();
							list.add( new Pair(i,j) );
							x = i;
							y = j;
							int k = 1;
							for(;k<word.length();k++){
								x += offset[p][0];
								y += offset[p][1];
								if( matrix[x][y]==word.charAt(k) )
									list.add( new Pair(x,y) );
								else
									break;								
							}
							if( k==lw )
								res.add( list );							
						}
					}					
				}
			}
		}		
		return res;
	}
}
