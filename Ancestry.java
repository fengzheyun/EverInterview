package Test_EA;

import java.io.IOException;
import java.util.*;


public class Ancestry {
	
	static Ancestry p = new Ancestry();

	/** 
	 * Given a query string made up of only letters, you are asked to write a function to find the most similar string 
	 * from a database represented by an array of strings. 
	 * 
	 * The similarity between the query string and a string in the database is calculated as follows:  
	 * 	1. F, S, and T denote the top three most frequent characters in the query string. For example, given “aaaabbbbbcdddeefs”, F='b', S='a', and T='d'. 
	 * 	2. For the query string and a string in the database,  
	 * 		F_num = the total number of occurrence of the letter F in both strings  
	 * 		S_num = the total number of occurrence of the letter S in both strings 
	 * 		T_num = the total number of occurrence of the letter T in both strings   
	 * 	3. The similarity between the two strings is the geometric mean of F_num, S_num and T_num:  (F_num*S_num*T_num)^(1/3)
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String queryString = "abb";//"aaaabbbbbcdddeefs";
		String[]  database = {"bf","ag", "dt","z"};
		String str = p.similarString(queryString, database);
		System.out.println( 0==str.length()?"No string is similar (contains any of the top three letter) as the query.":str );
	}
	
	private static final double ONE_THIRD = 1.0/3.0;
	
	public String similarString(String queryString, String[]  database){
		if( null==queryString || 0==queryString.length() ||  null==database || 0==database.length)
			return "";
		
		char[] FST = {' ',' ',' '};
		int[] freqs = new int[3];
		getTop3Chars( queryString, FST, freqs );
		if( 0==freqs[0])
			return "";
		double lowestSim = 0==freqs[1]?freqs[0]:( 0==freqs[2]? Math.sqrt(freqs[0]*freqs[1]):Math.pow(freqs[0]*freqs[1]*freqs[2], ONE_THIRD) );
		
		double maxVal = 0;
		int d_index = -1;
		for(int i=0;i<database.length;i++){
			String ds = database[i].toLowerCase();
			int nF = freqs[0], nS = freqs[1], nT = freqs[2];
			for( char ch:ds.toCharArray() ){
				if( ch>='a' && ch<='z'){
					if( ch==FST[0] )
						nF++;
					else if(ch==FST[1])
						nS++;
					else if(ch==FST[2])
						nT++;
				}
			}
			
			double sim = 0==freqs[1]?nF:( 0==freqs[2]? Math.sqrt(nF*nS):Math.pow(nF*nS*nT, ONE_THIRD) );
			if( sim>maxVal ){
				maxVal = sim;
				d_index = i;
			}
		}
		return maxVal>lowestSim?database[d_index]:"";
	}
	
	public void getTop3Chars( String query, char[] FST, int[] freqs ){
		query = query.toLowerCase();
		Map<Character, Integer> map = new HashMap<>();
		for(char ch:query.toCharArray()){
			if( ch>='a' && ch<='z'){
				map.put(ch, map.containsKey(ch)?1+map.get(ch):1);
			}
		}
		
		for( char ch:map.keySet() ){
			int num = map.get( ch );
			if( num>freqs[0] ){
				freqs[2] = freqs[1]; FST[2] = FST[1];
				freqs[1] = freqs[0]; FST[1] = FST[0];
				freqs[0] = num; FST[0] = ch;
			}else if( num>freqs[1]){
				freqs[2] = freqs[1]; FST[2] = FST[1];
				freqs[1] = num; FST[1] = ch;
			}else if( num>freqs[2]){
				freqs[2] = num; FST[2] = ch;
			}	
		}
	}
}
