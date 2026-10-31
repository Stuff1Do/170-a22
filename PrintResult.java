import java.io.FileWriter;
import java.io.PrintWriter;

class PrintResult{
	private FileWriter writer;
	private PrintWriter print;
	private String filename;
	PrintResult(String filename){
		this.filename=filename;
		try{
			writer=new FileWriter(new java.io.File(filename));
			print= new PrintWriter(writer);
			}
		catch(Exception e){
			e.printStackTrace();
			}
		}
	protected void print(Object text){
		print.print(text+"\n");
		}
	protected void close(){
		print.close();
		}
	protected void delete(){
		new java.io.File(filename).delete();
		}
	// current time in milliseconds (safe to subtract across minute/hour boundaries)
	protected double getTime(){
		return System.currentTimeMillis();
		}
	}
