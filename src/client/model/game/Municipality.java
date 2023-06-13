package client.model.game;

import client.utils.Config;

public class Municipality
{
    public final int index;
    public final String name;

    public Player owner;
    public int numDice;

    public Municipality(int index, String name)
    {
        this.index = index;
        this.name = name;
        this.numDice = Dice.getRandom(Config.MIN_START_DICE, Config.MAX_START_DICE);
        this.owner = null;
    }

    /**
     * @return true if given player is this municipality's owner. Returns false if municipality has
     *     no owner.
     */
    public boolean isOwner(Player player)
    {
        if (this.owner == null) return false;
        return this.owner.equals(player);
    }

    /**
     * Attack another municipality and update both municipalities number of dice and owner based on
     * the result.
     *
     * @param defender the municipality which is attacked
     */
    public boolean attack(Municipality defender)
    {
        if (!this.canAttack(defender))
            throw new RuntimeException("Cannot attack " + defender + " from " + this);

        boolean won = AttackLogic.getAttackResult(this.numDice, defender.numDice);
        if (won) defender.owner = this.owner;
        int[] newDice = AttackLogic.getDiceAfterAttack(this.numDice, defender.numDice, won);
        this.numDice = newDice[0];
        defender.numDice = newDice[1];
        return won;
    }

    /**
     * @return true only if this municipality is capable of attacking the given municipality.
     */
    public boolean canAttack(Municipality defender)
    {
        if (this.numDice < 2) return false;
        if (this.owner == null) return false;
        if (defender.isOwner(this.owner)) return false;
        if (!this.isNeighbor(defender.index)) return false;
        return true;
    }

    /**
     * @param otherIndex
     * @return true if the municipality with the given index is a neighbor of this municipality.
     */
    public boolean isNeighbor(int otherIndex)
    {
        for (int neighbor : NEIGHBORS[this.index])
        {
            if (neighbor == otherIndex) return true;
        }
        return false;
    }

    /**
     * @param municipalities full list of municipalities in correct order
     * @return the municipalities corresponding to the NEIGHBORS array.
     */
    public Municipality[] getNeighbors(Municipality[] municipalities)
    {
        Municipality[] neighbors = new Municipality[NEIGHBORS[this.index].length];
        for (int i = 0; i < neighbors.length; i++)
        {
            neighbors[i] = municipalities[NEIGHBORS[this.index][i]];
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Municipality)) return false;
        Municipality other = (Municipality)obj;
        return this.index == other.index;
    }

    @Override
    public String toString()
    {
        return String.format(
            "Municipality(id=%s, name=%s, owner=%s, numDice=%s)",
            this.index,
            this.name,
            this.owner,
            this.numDice
        );
    }

    public static final int LIERNE = 0;
    public static final int SNAASA = 1;
    public static final int GRONG = 2;
    public static final int OVERHALLA = 3;
    public static final int NAMSOS = 4;
    public static final int STEINKJER = 5;
    public static final int INDEROEY = 6;
    public static final int NAMDALSEID = 7;
    public static final int VERDAL = 8;
    public static final int FLATANGER = 9;
    public static final int OSEN = 10;
    public static final int ROAN = 11;
    public static final int AAFJORD = 12;
    public static final int VERRAN = 13;
    public static final int RINNDAL = 14;
    public static final int MERAAKER = 15;
    public static final int STJOERDAL = 16;
    public static final int LEVANGER = 17;
    public static final int LEKSVIK = 18;
    public static final int RISSA = 19;
    public static final int BJUGN = 20;
    public static final int TYDAL = 21;
    public static final int SELBU = 22;
    public static final int MALVIK = 23;
    public static final int TRONDHEIM = 24;
    public static final int KLAEBU = 25;
    public static final int MELHUS = 26;
    public static final int AGDENES = 27;
    public static final int SKAUN = 28;
    public static final int ORKDAL = 29;
    public static final int MELDAL = 30;
    public static final int SURNADAL = 31;
    public static final int ROEROS = 32;
    public static final int HOLTALEN = 33;
    public static final int MIDTREGAULDAL = 34;
    public static final int SUNNDAL = 35;
    public static final int RENNEBU = 36;
    public static final int OPPDAL = 37;
    public static final int SNILLFJORD = 38;
    public static final int HEMNE = 39;
    public static final int HITRA = 40;
    public static final int AURE = 41;

    /* public static final int COUNT = 42;

    /**
     * The correct spelling of all the municipalities.
     *
     * Example to get the name of Trondheim:
     * <pre>{@code
     * String name = Municipalities.NAMES[Municipalities.TRONDHEIM];
     * }</pre>
     */
    public static final String[] NAMES = new String[] {
        "Lierne",    "Snåsa",     "Grong",       "Overhalla", "Namsos",         "Steinkjær",
        "Inderøy",   "Namdaleid", "Verdal",      "Flatanger", "Osen",           "Roan",
        "Åfjord",    "Verran",    "Rinndal",     "Meråker",   "Stjørdal",       "Levanger",
        "Leksvik",   "Rissa",     "Bjugn",       "Tydal",     "Selbu",          "Malvik",
        "Trondheim", "Klæbu",     "Melhus",      "Agdenes",   "Skaun",          "Orkdal",
        "Meldal",    "Surnadal",  "Røros",       "Holtalen",  "Midtre Gauldal", "Sunndal",
        "Rennebu",   "Oppdal",    "Snillefjord", "Hemne",     "Hitra",          "Aure"
    };

    /**
     * Two-dimensional array where each element is a list of
     * indices of municipalities that border to a specific municipality.
     *
     * Example to get the neighbors of Trondheim:
     * <pre>{@code
     * int[] neighbors = Municipalities.NEIGHBORS[Municipalities.TRONDHEIM];
     * }</pre>
     */
    public static final int[][] NEIGHBORS = new int[][] {
  /* LIERNE */
        { SNAASA, GRONG },
 /* SNAASA  */
        { LIERNE, GRONG, STEINKJER, VERDAL, OVERHALLA },
 /* GRONG */
        { LIERNE, SNAASA, OVERHALLA },
 /* OVERHALLA */
        { GRONG, NAMSOS, STEINKJER },
 /* NAMSOS */
        { OVERHALLA, STEINKJER, NAMDALSEID },
 /* STEINKJER */
        { VERDAL, SNAASA, OVERHALLA, NAMSOS, NAMDALSEID, VERRAN, INDEROEY },
 /* INDEROEY */
        { LEKSVIK, VERRAN, LEVANGER, STEINKJER },
 /* NAMDALEID */
        { NAMSOS, FLATANGER, OSEN, ROAN, STEINKJER, VERRAN },
 /* VERDAL */
        { STEINKJER, SNAASA, LEVANGER, MERAAKER },
 /* FLATANGER */
        { NAMDALSEID, OSEN },
 /* OSEN */
        { FLATANGER, ROAN, NAMDALSEID },
 /* ROAN */
        { OSEN, AAFJORD, NAMDALSEID },
 /* AFJORD */
        { VERRAN, ROAN, BJUGN, RISSA, NAMDALSEID },
 /* VERRAN */
        { AAFJORD, RISSA, NAMDALSEID, INDEROEY, STEINKJER },
 /* RINNDAL */
        { SURNADAL, MELDAL, OPPDAL, RENNEBU, HEMNE },
 /* MERAAKER */
        { VERDAL, TYDAL, SELBU, STJOERDAL, LEVANGER },
 /* STJOERDAL */
        { MERAAKER, LEVANGER, VERDAL, SELBU },
 /* LEVANGER */
        { VERDAL, MERAAKER, STJOERDAL },
 /* LEKVSIK */
        { INDEROEY, RISSA, VERRAN },
 /* RISSA */
        { LEKSVIK, BJUGN, AAFJORD, AGDENES, VERRAN },
 /* BJUGN */
        { RISSA, AAFJORD, AGDENES },
 /* TYDAL */
        { ROEROS, HOLTALEN, SELBU, MERAAKER },
 /* SELBU */
        { STJOERDAL, MALVIK, TRONDHEIM, KLAEBU, MIDTREGAULDAL, HOLTALEN, TYDAL, MERAAKER },
 /* MALVIK */
        { STJOERDAL, KLAEBU, SELBU, TRONDHEIM },
 /* TRONDHEIM */
        { MALVIK, SELBU, KLAEBU, MELHUS },
 /* KLAEBU */
        { TRONDHEIM, MALVIK, SELBU, MELHUS },
 /* MELHUS */
        { TRONDHEIM, KLAEBU, MIDTREGAULDAL, SKAUN, MELDAL },
 /* AGDENES */
        { SNILLFJORD, RISSA, ORKDAL },
 /* SKAUN */
        { MELHUS, ORKDAL },
 /* ORKDAL */
        { AGDENES, SNILLFJORD, SKAUN, HEMNE, MELDAL },
 /* MELDAL */
        { MELHUS, ORKDAL, RINNDAL, RENNEBU, MIDTREGAULDAL },
 /* SURNADAL */
        { SUNNDAL, RINNDAL, AURE, HEMNE, OPPDAL },
 /* ROROS */
        { TYDAL, HOLTALEN },
 /* HOLTALLEN */
        { ROEROS, TYDAL, SELBU, MIDTREGAULDAL },
 /* MIDTREGAULDAL */
        { HOLTALEN, SELBU, MELHUS, RENNEBU, MELDAL },
 /* SUNNDAL */
        { OPPDAL, SURNADAL, AURE },
 /* RENNEBU */
        { OPPDAL, MIDTREGAULDAL, MELDAL, RINNDAL },
 /* OPPDAL */
        { SUNNDAL, SURNADAL, RINNDAL, RENNEBU },
 /* SNILLFJORD */
        { AGDENES, HEMNE, ORKDAL, HITRA },
 /* HEMNE */
        { HITRA, AURE, SNILLFJORD, RINNDAL, SURNADAL, ORKDAL },
 /* HITRA */
        { HEMNE, SNILLFJORD, AURE },
 /* AURE */
        { HITRA, HEMNE, SURNADAL, SUNNDAL }
    };
}
