/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.metrics.collector.endpoint.filters;

import com.google.inject.Inject;

import com.ning.metrics.serialization.util.Managed;
import com.ning.metrics.collector.events.parsing.ExtractedAnnotation;
import com.ning.metrics.collector.util.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class PatternSetFilter implements Filter<ExtractedAnnotation>
{
    private final FieldExtractor fieldExtractor;
    private final ConcurrentMap<String, Pattern> patternMap = new ConcurrentHashMap<String, Pattern>();

    @Inject
    public PatternSetFilter(FieldExtractor fieldExtractor, Set<Pattern> patterns)
    {
        this.fieldExtractor = fieldExtractor;

        for (Pattern pattern : patterns) {
            patternMap.put(pattern.toString(), pattern);
        }
    }

    @Override
    public boolean passesFilter(String name, ExtractedAnnotation annotation)
    {
        String input = fieldExtractor.getField(name, annotation);

        if (input == null) {
            return false;
        }

        for (Pattern pattern : patternMap.values()) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }

        return false;
    }

    @Managed(description = "list of patterns for this filter")
    public List<String> getPatternSet()
    {
        return new ArrayList<String>(patternMap.keySet());
    }

    @Managed(description = "add a regular expression to filter set")
    public void addPattern(String patternString)
    {
        patternMap.put(patternString, Pattern.compile(patternString));
    }

    @Managed(description = "add a regular expression to filter set")
    public void removePattern(String patternString)
    {
        patternMap.remove(patternString);
    }
}
