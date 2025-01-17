/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.sdk.io.aws2.dynamodb;

import static org.apache.beam.vendor.guava.v20_0.com.google.common.base.Preconditions.checkArgument;

import java.net.URI;
import javax.annotation.Nullable;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

/** Basic implementation of {@link DynamoDbClientProvider} used by default in {@link DynamoDBIO}. */
public class BasicDynamoDbClientProvider implements DynamoDbClientProvider {
  private final AwsCredentialsProvider awsCredentialsProvider;
  private final String region;
  @Nullable private final URI serviceEndpoint;

  BasicDynamoDbClientProvider(
      AwsCredentialsProvider awsCredentialsProvider, String region, @Nullable URI serviceEndpoint) {
    checkArgument(awsCredentialsProvider != null, "awsCredentialsProvider can not be null");
    checkArgument(region != null, "region can not be null");
    this.awsCredentialsProvider = awsCredentialsProvider;
    this.region = region;
    this.serviceEndpoint = serviceEndpoint;
  }

  @Override
  public DynamoDbClient getDynamoDbClient() {
    DynamoDbClientBuilder builder =
        DynamoDbClient.builder()
            .credentialsProvider(awsCredentialsProvider)
            .region(Region.of(region));

    if (serviceEndpoint != null) {
      builder.endpointOverride(serviceEndpoint);
    }

    return builder.build();
  }
}
