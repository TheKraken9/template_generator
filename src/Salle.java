
public class Salle {
	private int id;
	private String nom_salle;
	private int capacite;

	public void setId( int id )
	{
		this.id = id; 
	}

	public void setNom_salle( String nom_salle )
	{
		this.nom_salle = nom_salle; 
	}

	public void setCapacite( int capacite )
	{
		this.capacite = capacite; 
	}


	public int getId()
	{
		return this.id; 
	}

	public String getNom_salle()
	{
		return this.nom_salle; 
	}

	public int getCapacite()
	{
		return this.capacite; 
	}


	public Salle(int id, String nom_salle, int capacite )
	{
		this.setId(id); 
		this.setNom_salle(nom_salle); 
		this.setCapacite(capacite); 
	}
	public Salle()
	{

	}
}
