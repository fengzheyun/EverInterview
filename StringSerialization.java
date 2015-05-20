// Java Serialization: Converting an object to a byte stream, so that it can be decoded back into an object
// Convert a List<String> object into a String object so that it can be decoded back.
//   encoder: List<String> --> String
//   decoder: String --> List<String>
// Example: ["hello", "world", "welome", "2", "EA"]
// output: "hello word welcome 2 EA", int[] arr = {5, 4, 7, 1 2};
// "5-hello4-word7-welcome1-22-EA"
// int  indexOf(int ch, int fromIndex)

public class StringEncoder{
    
    public StringEncoder(){}
    
    public void test_cases(){
        
    }
    
    private static final char DELIMITER = '-';
    
    public List<String> decoder( String s ){
        List<String> res = new ArrayList<String>();
        if( null==s || 0==s.length())
            return res;
            
        int i = 0;
        while(i<s.length()){           
            int index = s.indexOf(DELIMITER, i);
            int len = Integer.parseInteger( s.substring(i,index) );
            res.add( s.substring(index+1, index+len+1) );
            i = index+len+1; 
        }
        return res;
    }

    public String encoder(List<String> input){
        if( null==input || 0==input.size() )
            return "";
        
        StringBuilder res = new StringBuilder();
        for(String s:input){
            int len = s.length();
            res.append( len+DELIMITER+s);
        }
        return res.toString();
    }
}
