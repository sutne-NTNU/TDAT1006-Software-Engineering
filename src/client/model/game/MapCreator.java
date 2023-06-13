package client.model.game;

import java.util.ArrayList;

public class MapCreator
{
    /**
     * Initializes all municipalities for the map, assigning an equal number of municipalities to
     * each player. Remaining municipalities are 'neutral' and have no owner.
     */
    public static Municipality[] createMunicipalities(Player[] players)
    {
        if (players.length < 2) throw new IllegalArgumentException("Not enough players");
        // Create all municipalities, without any owners
        Municipality[] municipalities = new Municipality[Municipality.NAMES.length];
        for (int i = 0; i < municipalities.length; i++)
        {
            municipalities[i] = new Municipality(i, Municipality.NAMES[i]);
        }

        // Assign an equal number of municipalities to each player
        int numMunicipalitiesPerUser = Municipality.NAMES.length / players.length;
        for (Player player : players)
        {
            for (int i = 0; i < numMunicipalitiesPerUser; i++)
            {
                Municipality municipality = getRandomUnownedMunicipality(municipalities);
                municipality.owner = player;
            }
        }
        return municipalities;
    }

    private static Municipality getRandomUnownedMunicipality(Municipality[] municipalities)
    {
        ArrayList<Municipality> unowned = new ArrayList<Municipality>();
        for (int i = 0; i < municipalities.length; i++)
        {
            if (municipalities[i].owner != null) continue;
            unowned.add(municipalities[i]);
        }
        if (unowned.isEmpty()) throw new IllegalArgumentException("No unowned municipalities");
        return Dice.selectRandom(unowned);
    }
}
