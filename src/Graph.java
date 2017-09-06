import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class Graph {

	//working for undirected graph
	
	HashMap<Character,ArrayList<Integer>> L=new HashMap<>();//storing available
	//colors for vertices
	String a;
	String b;
	String c;
	TreeMap<Character,LinkedList<Character>> graph=new TreeMap<>();//main graph 
	//using map with a vertex as key and all the adjacent nodes in a linkedlist
	//attached with this vertex.
	int delta;
	int color_start;
	ArrayList<Integer> total_color=new ArrayList<>();//Total available colors
	ArrayList<ArrayList<Integer>> color_matrix=new ArrayList<>();//storing colors of edges
	
	public void expandMatrix()
	{
		int size=color_matrix.size();
		ArrayList<Integer> al=new ArrayList<>(size);
		int temp=size;
		while(temp>0)
		{
			temp--;
			al.add(0);
		}
		color_matrix.add(size, al);
		for(int i=0;i<color_matrix.size();i++)
		{
			color_matrix.get(i).add(size, 0);
		}
	}
	public void addColor(char u,char v,int color)
	{
		if(!total_color.contains(color))
		total_color.add(color);
		int i=u-'a';
		int j=v-'a';
		//assumption we are expanding matrix in order with vertices coming like a,b,c,d,....
		if(color_matrix.size()-1<i)
		{
			expandMatrix();
		}
		if(color_matrix.size()-1<j)
		{
			expandMatrix();
		}
		color_matrix.get(i).set(j, color);
		color_matrix.get(j).set(i, color);
		
		 
	}
	public void addEdge(char u,char v)
	{
		if(!graph.containsKey(u))
		{
			graph.put(u, new LinkedList<>());			
		}
		graph.get(u).addLast(v);
		
		//another entry as graph it undirected
		if(!graph.containsKey(v))
		{
			graph.put(v, new LinkedList<>());			
		}
		graph.get(v).addLast(u);
		
	}
	public void updateDelta()
	{
		int max=-1;
		for(char c:graph.keySet())
		{
			
			max=Math.max(max,graph.get(c).size());
		}
		delta=max;
	}
	public void lemma1(char u,char v, int no_of_ears,ArrayList<Integer> Lu,int vertex_start)
	{
		//System.out.println("Vertex start:"+(char)vertex_start+"Lu.size():"+Lu.toString());
		for(int i=1;i<=no_of_ears;i++)
		{
			addColor(u,(char)vertex_start,Lu.get(i-1));
			if(i==no_of_ears)
			{
			//	System.out.println("kjjmhgj"+(char)vertex_start);
				addColor(v,(char)vertex_start,Lu.get(0));
			}
			else addColor(v,(char)vertex_start,Lu.get(i));
			
			vertex_start++;
		}
		
		updateDelta();
	}
	//here in lemma2, no_of_ears=Lu.size()=lv.size()
	public void lemma2(char u,char v,ArrayList<Integer> Lu,ArrayList<Integer> Lv,int vertex_start,int no_of_ears)
	{
		ArrayList<Integer> I=new ArrayList<>();
		//finding intersection of Lu and Lv
		for(int num:Lu)
		{
			if(Lv.contains(num))
			{
				I.add(num);
			}
		}
		//finding Lu-I
		int t=no_of_ears;
		int ii=I.size();
		ArrayList<Integer> LuI=(ArrayList<Integer>)Lu.clone();
		ArrayList<Integer> LvI=(ArrayList<Integer>)Lv.clone();
		for(int i=0;i<Lu.size();i++)
		{
			if(I.contains(Lu.get(i)))
			{
				LuI.remove(new Integer(Lu.get(i)));
			}
		}
		//finding Lv-I
		for(int i=0;i<Lv.size();i++)
		{
			if(I.contains(Lv.get(i)))
			{
				LvI.remove(new Integer(Lv.get(i)));
			}
		}
		Collections.sort(Lu);
		Collections.sort(Lv);
		Collections.sort(I);
		Collections.sort(LuI);
//		System.out.println("Lu:"+Lu.toString());
//		
//		System.out.println("Lv:"+Lv.toString());
//		System.out.println("LuI:"+LuI.toString());
//		System.out.println("LvI:"+LvI.toString());
//		System.out.println("I:"+I.toString());
		ArrayList<Character> temp_vertices=new ArrayList<>();
		for(int i=1;i<=ii;i++)
		{
			//here color_start does not necessarily mean vertex start letter
			//int vertex_start=graph.size()+'a';
			//System.out.println((char)vertex_start);
			char to=(char)(vertex_start);
			temp_vertices.add(to);
			//addEdge(u,to);
			addColor(u,to,I.get(i-1));
			vertex_start++;
		}
		for(int i=ii+1;i<=t;i++)
		{
			//here color_start does not necessarily mean vertex start letter
			//int vertex_start=graph.size()+'a';
			//System.out.println((char)vertex_start);
			char to=(char)(vertex_start);
			temp_vertices.add(to);
			//addEdge(u,to);
			addColor(u,to,LuI.get(i-ii-1));
			vertex_start++;
		}
		int start=0;
		for(int i=1;i<=ii-1;i++)
		{
			char to=temp_vertices.get(start);
			start++;
			//addEdge(v,to);
			addColor(v,to,I.get(i));
		}
		for(int i=ii;i<=t-1;i++)
		{
			char to=temp_vertices.get(start);
			start++;
			//addEdge(v,to);
			addColor(v,to,LvI.get(i-ii));
		}
		char to=temp_vertices.get(start);
		start++;
		//addEdge(v,to);
		addColor(v,to,Lu.get(0));
		updateDelta();
	}
	public Graph()
	{

		
		//constructing edges
		addEdge('a','b');
		addEdge('b','c');
		addEdge('a','c');
		//color
		addColor('a','b',1);
		addColor('b','c',2);
		addColor('a','c',3);
		color_start=4;
		updateDelta();
	}
	public void printGraph()
	{
		for(Character c:graph.keySet())
		{
			System.out.print(c+"-->");
			for(Character p:graph.get(c))
			{
				System.out.print(p+" ");
			}
			System.out.println();
		}
	}
	public void printColorMatrix()
	{
		System.out.print("  ");
		for(int i=0;i<color_matrix.size();i++)
		{
			char c=(char)('a'+i);
			System.out.print(Character.toString(c)+" ");
		}
		System.out.println();
		for(int i=0;i<color_matrix.size();i++)
		{
			char c=(char)('a'+i);
			System.out.print(Character.toString(c)+" ");
			ArrayList<Integer> al=color_matrix.get(i);
			for(int j=0;j<al.size();j++)
			{
			  System.out.print(al.get(j)+" ");	
			}
			System.out.println();
		}
	}
	public void printTotalColor()
	{
		for(int num:total_color)
		{
			System.out.print(num+" ");
		}		
	}
	//try a function to figure out available color for a particular vertex
	public void printResult()
	{
		System.out.println(
				" Max Degree: "+delta+" Total colors:"+total_color.size());
	}
	//find common colors from Lu and Lv
	public ArrayList<Integer> commonColor(ArrayList<Integer> Lu,ArrayList<Integer> Lv,
			int k)
	{
		
		ArrayList<Integer> I=new ArrayList<>();
		//finding intersection of Lu and Lv
		for(int num:Lu)
		{
			if(Lv.contains(num))
			{
				I.add(num);
			}
		}
		Collections.sort(I);
		if(I.size()>=k)
		{
			while(I.size()!=k)
			{
				I.remove(I.size()-1);
			}
			return I;
		}
		else return null;
	}
	public ArrayList<Integer> calculateL(char u,int delta)
	{
		ArrayList<Integer> Lu=new ArrayList<>();
		ArrayList<Integer> color=color_matrix.get(u-'a');
		for(int i=1;i<=delta+1;i++)
		{
			if(!color.contains(i))
			{
				Lu.add(i);
			}
		}
		return Lu;
	}
	public int addEar(char u,char v,int no_of_ears)
	{
		int vertex_start=graph.size()+'a';
		for(int i=1;i<=no_of_ears;i++)
		{
			addEdge(u,(char)vertex_start);
			addEdge(v,(char)vertex_start);
			vertex_start++;
		}
		updateDelta();
		return delta;
	}
	public void print()
	{
		System.out.println("***Graph***");
		printGraph();
		System.out.println("***Color Matrix***");
		printColorMatrix();
		System.out.println("***Result Proof***");
		printResult();
		System.out.println();
	}
	public static void main(String[]args)
	{
		//construct triangle
		Graph ob=new Graph();
		//1
		ob.print();		
		//adding 3 ears on a-b
		int vertex_start=ob.graph.size()+'a';
		int degree=ob.addEar('a', 'b', 3);
		ArrayList<Integer> Lu=ob.calculateL('a', degree);
		ArrayList<Integer> Lv=ob.calculateL('b', degree);
		ArrayList<Integer> I=ob.commonColor(Lu, Lv, 3);
		if(I==null)
		{
		//	System.out.println("Lemma2 called");
			ob.lemma2('a', 'b', Lu, Lv, vertex_start,3);
		}
		else 
		{
		//	System.out.println("Lemma1 called");
			ob.lemma1('a', 'b',3,I,vertex_start);
		}
		//2
		ob.print();
		
		//adding 3 ears on b-c
		vertex_start=ob.graph.size()+'a';
		degree=ob.addEar('b', 'c', 3);
		Lu=ob.calculateL('b', degree);
		Lv=ob.calculateL('c', degree);
		I=ob.commonColor(Lu, Lv, 3);
		if(I==null)
		{
		//	System.out.println("Lemma2 called");
			ob.lemma2('b', 'c', Lu, Lv, vertex_start,3);
		}
		else 
		{
		//	System.out.println("Lemma1 called");
			ob.lemma1('b', 'c',3,I,vertex_start);
		}
		//3
		ob.print(); 		
		
		//adding 4 ears on a-c
		vertex_start=ob.graph.size()+'a';
		degree=ob.addEar('a', 'c', 4);
		Lu=ob.calculateL('a', degree);
		Lv=ob.calculateL('c', degree);
		I=ob.commonColor(Lu, Lv, 4);
		if(I==null)
		{
		//	System.out.println("Lemma2 called");
			ob.lemma2('a', 'c', Lu, Lv, vertex_start,4);
		}
		else 
		{
		//	System.out.println("Lemma1 called");
			ob.lemma1('a', 'c',4,I,vertex_start);
		}
		//4
		ob.print(); 
		
		//adding 4 ears on a-d
		vertex_start=ob.graph.size()+'a';
		//System.out.println(ob.graph.size());
		degree=ob.addEar('a', 'd', 4);
		//System.out.println(ob.graph.size());
		Lu=ob.calculateL('a', degree);
		int x=ob.color_matrix.get('b'-'a').get('d'-'a');
		Lu.remove(new Integer(x));
		Lv=ob.calculateL('d', degree);
		I=ob.commonColor(Lu, Lv, 4);
		if(I==null)
		{
		//	System.out.println("Lemma2 called");
			ob.lemma2('a', 'd', Lu, Lv, vertex_start,4);
		}
		else 
		{
		//	System.out.println("Lemma1 called");
			ob.lemma1('a', 'd',4,I,vertex_start);
		}
		//5
		ob.print(); 	
		
		//adding 4 ears on b-d
				vertex_start=ob.graph.size()+'a';
				//System.out.println(ob.graph.size());
				degree=ob.addEar('b', 'd', 4);
			//	System.out.println(ob.graph.size());
				Lu=ob.calculateL('b', degree);
				x=ob.color_matrix.get('a'-'a').get('d'-'a');
				Lu.remove(new Integer(x));
				Lv=ob.calculateL('d', degree);
				I=ob.commonColor(Lu, Lv, 4);
				if(I==null)
				{
				//	System.out.println("Lemma2 called");
					ob.lemma2('b', 'd', Lu, Lv, vertex_start,4);
				}
				else 
				{
				//	System.out.println("Lemma1 called");
					ob.lemma1('b', 'd',4,I,vertex_start);
				}
				//6
				ob.print(); 	

		
	}
	
}
