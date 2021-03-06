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

import com.ning.metrics.collector.events.parsing.ExtractedAnnotation;
import com.ning.metrics.collector.util.Filter;

import java.util.List;

@SuppressWarnings("unchecked")
public class OrFilter implements Filter<ExtractedAnnotation>
{
    private final List<Filter> filterList;

    @Inject
    public OrFilter(List<Filter> filterList)
    {
        this.filterList = filterList;
    }

    @Override
    public boolean passesFilter(String eventName, ExtractedAnnotation annotation)
    {
        for (Filter filter : filterList) {
            if (filter.passesFilter(eventName, annotation)) {
                return true;
            }
        }

        return false;
    }
}
