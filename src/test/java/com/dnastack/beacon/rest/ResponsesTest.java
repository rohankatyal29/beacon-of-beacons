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
package com.dnastack.beacon.rest;

import com.dnastack.beacon.service.BeaconResponse;
import com.google.common.collect.ImmutableSet;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.dnastack.beacon.rest.util.ResponseTestUtils.beaconsMatch;
import static com.dnastack.beacon.rest.util.ResponseTestUtils.queriesMatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test of responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ResponsesTest extends BasicTest {

    public static final String QUERY_BEACON_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_BEACON_WITH_REF_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s&ref=%s";
    public static final String QUERY_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_WITH_REF_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s&ref=%s";
    private static final Set<String> BEACON_IDS = ImmutableSet.of("clinvar", "uniprot", "lovd", "ebi", "ncbi", "wtsi", "amplab", "kaviar");

    protected static String getUrl(String b, String[] params) {
        String res = null;

        if (params.length == 4) {
            if (params[3] == null) {
                res = String.format(QUERY_BEACON_TEMPLATE, b, params[0], params[1], params[2]);
            } else {
                res = String.format(QUERY_BEACON_WITH_REF_TEMPLATE, b, params[0], params[1], params[2], params[3]);
            }
        }

        return res;
    }

    protected static String getUrl(String[] params) {
        String res = null;

        if (params.length == 4) {
            if (params[3] == null) {
                res = String.format(QUERY_TEMPLATE, params[0], params[1], params[2]);
            } else {
                res = String.format(QUERY_WITH_REF_TEMPLATE, params[0], params[1], params[2], params[3]);
            }
        }

        return res;
    }

    @SuppressWarnings("unchecked")
    public static List<BeaconResponse> readResponses(String url) throws JAXBException, MalformedURLException {
        return (List<BeaconResponse>) readObject(BeaconResponse.class, url);
    }

    @Test
    public void testAllResponses(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] q = {"13", "32888799", "G", null};
        List<BeaconResponse> brs = readResponses(url.toExternalForm() + getUrl(q));

        assertEquals(brs.size(), BEACON_IDS.size());
        for (BeaconResponse br : brs) {
            assertTrue(BEACON_IDS.contains(br.getBeacon().getId()));
            assertTrue(queriesMatch(br.getQuery(), q));
        }
    }

    @Test
    public void testAllResponsesWithSpecificRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] q = {"13", "32888799", "G", "hg19"};
        List<BeaconResponse> brs = readResponses(url.toExternalForm() + getUrl(q));

        assertEquals(brs.size(), BEACON_IDS.size());
        for (BeaconResponse br : brs) {
            System.out.println(br.toString());
            assertTrue(BEACON_IDS.contains(br.getBeacon().getId()));
            assertTrue(queriesMatch(br.getQuery(), q));
        }
    }

    @Test
    public void testSpecificResponseWithSpecificRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "clinvar";
        String[] q = {"1", "10042538", "T", "hg19"};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForClinvar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "clinvar";
        String[] q = {"1", "10042538", "T", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForUniprot(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "uniprot";
        String[] q = {"1", "977028", "T", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForLovd(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "lovd";
        String[] q = {"1", "808922", "T", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForAmplab(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "amplab";
        String[] q = {"15", "41087870", "A", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForKaviar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "kaviar";
        String[] q = {"1", "808922", "A", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForNcbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "ncbi";
        String[] q = {"22", "17213590", "TGTTA", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForEbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "ebi";
        String[] q = {"13", "32888799", "G", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForWtsi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "wtsi";
        String[] q = {"1", "808922", "A", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForBob(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "bob";
        String[] q = {"13", "32888799", "G", null};
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

}
