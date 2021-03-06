package gaffer.accumulostore.operation.impl;

import gaffer.accumulostore.utils.Pair;
import gaffer.exception.SerialisationException;
import gaffer.jsonserialisation.JSONSerialiser;
import gaffer.operation.GetOperation;
import gaffer.operation.OperationTest;
import gaffer.operation.data.EntitySeed;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class GetEdgesInRangesTest implements OperationTest {
    private static final JSONSerialiser serialiser = new JSONSerialiser();

    @Test
    public void shouldNotReturnEntities(){

        final GetEdgesInRanges<Pair<EntitySeed>> op = new GetEdgesInRanges<>();
        assertFalse(op.isIncludeEntities());

    }

    @Test
    public void shouldNotBeAbleToSetNoEdges(){

        final GetEdgesInRanges<Pair<EntitySeed>> op = new GetEdgesInRanges<>();

        try {
            op.setIncludeEdges(GetOperation.IncludeEdgeType.NONE);
        } catch (final IllegalArgumentException e) {
            assertTrue(true);
            return;
        }
        fail();

    }

    @Test
    @Override
    public void shouldSerialiseAndDeserialiseOperation() throws SerialisationException {
        // Given
        List<Pair<EntitySeed>> pairList = new ArrayList<>();
        Pair<EntitySeed> pair1 = new Pair<>(new EntitySeed("source1"), new EntitySeed("destination1"));
        Pair<EntitySeed> pair2 = new Pair<>(new EntitySeed("source2"), new EntitySeed("destination2"));
        pairList.add(pair1);
        pairList.add(pair2);
        final GetEdgesInRanges<Pair<EntitySeed>> op = new GetEdgesInRanges<>(pairList);
        // When
        byte[] json = serialiser.serialise(op, true);

        final GetEdgesInRanges<Pair<EntitySeed>> deserialisedOp = serialiser.deserialise(json, GetEdgesInRanges.class);

        // Then
        final Iterator itrPairs = deserialisedOp.getSeeds().iterator();
        assertEquals(pair1, itrPairs.next());
        assertEquals(pair2, itrPairs.next());
        assertFalse(itrPairs.hasNext());

    }
}

