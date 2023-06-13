package client.model.game;

import javafx.scene.paint.Color;

public class PoliticalParty
{
    public final Color color;
    public final String name;
    public final String abbreviation;

    private PoliticalParty(String abbreviation, String name, String color)
    {
        this.name = name;
        this.color = Color.web(color);
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof PoliticalParty)) return false;
        PoliticalParty other = (PoliticalParty)obj;
        return this.name.equals(other.name);
    }

    public static PoliticalParty FRP
        = new PoliticalParty("FRP", "Fremskrittspartiet", "rgb(19,40,90)");
    public static PoliticalParty H = new PoliticalParty("H", "Høyre", "rgb(65,120,190)");
    public static PoliticalParty V = new PoliticalParty("V", "Venstre", "rgb(6,75,47)");
    public static PoliticalParty KRF
        = new PoliticalParty("KRF", "Kristelig Folkeparti", "rgb(243,164,59)");
    public static PoliticalParty MDG
        = new PoliticalParty("MDG", "Miljøpartiet De Grønne", "rgb(123,185,50)");
    public static PoliticalParty SP = new PoliticalParty("SP", "Senterpartiet", "rgb(21,150,74)");
    public static PoliticalParty AP = new PoliticalParty("AP", "Arbeiderpartiet", "rgb(205,34,44)");
    public static PoliticalParty SV
        = new PoliticalParty("SV", "Sosialistisk Venstreparti", "rgb(173,32,106)");
    public static PoliticalParty R = new PoliticalParty("R", "Rødt", "rgb(138,26,17)");

    public static PoliticalParty[] PARTIES
        = new PoliticalParty[] { AP, H, SP, V, SV, KRF, FRP, MDG, R };
}