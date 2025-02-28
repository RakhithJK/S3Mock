/*
 *  Copyright 2017-2022 Adobe.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.adobe.testing.s3mock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.List;

/**
 * DTO representing a list of buckets.
 * <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_Bucket.html">API Reference</a>
 */
@JsonRootName("Buckets")
public class Buckets {

  @JsonProperty("Bucket")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Bucket> buckets;

  public List<Bucket> getBuckets() {
    return buckets;
  }

  public void setBuckets(final List<Bucket> buckets) {
    this.buckets = buckets;
  }
}
