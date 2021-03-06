/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dnastack.bob.rest;

import com.dnastack.bob.dto.BeaconResponseTo;
import com.dnastack.bob.dto.ReferenceTo;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.beaconsMatch;
import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.getNonMachingFields;
import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.queriesMatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test of Cafe variome services.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class CafeVariomeResponseTest extends AbstractResponseTest {

    private static final String BEACON = "cafe-variome";

    @Override
    @Test
    public void testAllRefsFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"2", "179612320", "T", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse());
    }

    @Test
    public void testFoundCafeCardioKit(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "cafe-cardiokit";
        String[] query = {"2", "179393690", "T", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(b, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse());
    }

    @Test
    public void testFoundCafeCentral(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "cafe-central";
        String[] query = {"2", "179612320", "T", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(b, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testSpecificRefFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"2", "179612320", "T", "hg19"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testSpecificRefNotFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"2", "179612320", "T", "hg38"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse() == null || !br.getResponse());
    }

    @Test
    @Override
    public void testInvalidRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"17", "41197711", "G", "hg100"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertEquals(br.getQuery().getReference(), null);
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testRefConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"17", "41197711", "G", "grch37"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertEquals(br.getQuery().getReference(), ReferenceTo.HG19);
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testStringAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"17", "41197711", "TGT", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // unsupported
        assertTrue(br.getResponse() != true);
    }

    @Test
    @Override
    public void testDel(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"17", "41197711", "D", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // unsupported
        assertTrue(br.getResponse() != true);
    }

    @Test
    @Override
    public void testIns(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"17", "41197711", "I", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // unsupported
        assertTrue(br.getResponse() != true);
    }

    @Test
    @Override
    public void testAllRefsNotFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "41197713", "G", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testInvalidAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"17", "41197711", "DC", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertNull(br.getQuery().getAllele());
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testAlleleConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"17", "41197711", "g", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertEquals(query[2].toUpperCase(), br.getQuery().getAllele());
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testChromConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"chrom17", "41197711", "G", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue((getNonMachingFields(br.getQuery(), query).size() == 1) && (getNonMachingFields(br.getQuery(), query).contains(0)));
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testInvalidChrom(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"30", "41087869", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertNull(br.getQuery().getChromosome());
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testChromX(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"X", "31224683", "C", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testChromMT(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"MT", "41087869", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // unsupported
        assertTrue(br.getResponse() != true);
    }

    @Override
    public void testDifferentGenome(URL url) throws JAXBException, MalformedURLException {
        // intentionally empty
    }
}
