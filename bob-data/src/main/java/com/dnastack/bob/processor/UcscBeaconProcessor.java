/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
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
package com.dnastack.bob.processor;

import com.dnastack.bob.entity.Beacon;
import com.dnastack.bob.entity.Query;
import com.dnastack.bob.entity.Reference;
import com.google.common.collect.ImmutableSet;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;

import static com.dnastack.bob.util.HttpUtils.createRequest;
import static com.dnastack.bob.util.HttpUtils.executeRequest;
import static com.dnastack.bob.util.ParsingUtils.parseYesNoCaseInsensitive;
import static com.dnastack.bob.util.QueryUtils.denormalizeChromosome;

/**
 * A Genomics Alliance beacon service at UCSC.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Processor
@Ucsc
public class UcscBeaconProcessor extends AbstractBeaconProcessor {

    private static final long serialVersionUID = 13L;
    private static final String BASE_URL = "http://hgwdev-max.cse.ucsc.edu/cgi-bin/beacon/query";
    private static final String PARAM_TEMPLATE = "?track=%s&chrom=%s&pos=%d&allele=%s";
    private static final String CHROM_TEMPLATE = "chr%s";
    private static final Set<Reference> SUPPORTED_REFS = ImmutableSet.of(Reference.HG19);

    private String getQueryUrl(String track, String chrom, Long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, track, chrom, pos, allele);

        return BASE_URL + params;
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(Beacon beacon, Query query) {
        String res = null;
        try {
            res = executeRequest(createRequest(getQueryUrl(beacon.getId(), denormalizeChromosome(CHROM_TEMPLATE, query.getChromosome()), query.getPosition(), query.getAllele()), false, null));
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

    @Override
    @Asynchronous
    public Future<Boolean> parseQueryResponse(Beacon b, String response) {
        Boolean res = parseYesNoCaseInsensitive(response);

        return new AsyncResult<>(res);
    }

    @Override
    public Set<Reference> getSupportedReferences() {
        return SUPPORTED_REFS;
    }
}
