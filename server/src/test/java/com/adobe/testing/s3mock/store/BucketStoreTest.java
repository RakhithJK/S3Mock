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

package com.adobe.testing.s3mock.store;

import static org.assertj.core.api.Assertions.assertThat;

import com.adobe.testing.s3mock.dto.Bucket;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {DomainConfiguration.class})
class BucketStoreTest {

  private static final String TEST_BUCKET_NAME = "testbucket";
  private static final String ALL_BUCKETS = null;


  @Autowired
  private BucketStore bucketStore;

  @Autowired
  private File rootFolder;

  @MockBean
  private KmsKeyStore kmsKeyStore;

  @MockBean
  private FileStore fileStore;


  /**
   * Creates a bucket and checks that it exists.
   *
   */
  @Test
  void shouldCreateBucket() {
    final Bucket bucket = bucketStore.createBucket(TEST_BUCKET_NAME);
    assertThat(bucket.getName()).as("Bucket should have been created.").endsWith(TEST_BUCKET_NAME);
    assertThat(bucket.getPath()).exists();
  }

  /**
   * Checks if Bucket exists.
   *
   */
  @Test
  void bucketShouldExist() {
    bucketStore.createBucket(TEST_BUCKET_NAME);

    final Boolean doesBucketExist = bucketStore.doesBucketExist(TEST_BUCKET_NAME);

    assertThat(doesBucketExist).as(
            String.format("The previously created bucket, '%s', should exist!", TEST_BUCKET_NAME))
        .isTrue();
  }

  /**
   * Checks if bucket doesn't exist.
   */
  @Test
  void bucketShouldNotExist() {
    final Boolean doesBucketExist = bucketStore.doesBucketExist(TEST_BUCKET_NAME);

    assertThat(doesBucketExist).as(
        String.format("The bucket, '%s', should not exist!", TEST_BUCKET_NAME)).isFalse();
  }

  /**
   * Checks if created buckets are listed.
   *
   */
  @Test
  void shouldHoldAllBuckets() {
    final String bucketName1 = "myNüwNämeÄins";
    final String bucketName2 = "myNüwNämeZwöei";
    final String bucketName3 = "myNüwNämeDrü";

    bucketStore.createBucket(bucketName1);
    bucketStore.createBucket(bucketName2);
    bucketStore.createBucket(bucketName3);

    final List<Bucket> buckets = bucketStore.listBuckets();

    assertThat(buckets.size()).as("FileStore should hold three Buckets").isEqualTo(3);
  }

  /**
   * Creates a bucket an checks that it can be retrieved by it's name.
   *
   */
  @Test
  void shouldGetBucketByName() {
    bucketStore.createBucket(TEST_BUCKET_NAME);
    Bucket bucket = bucketStore.getBucket(TEST_BUCKET_NAME);

    assertThat(bucket).as("Bucket should not be null").isNotNull();
    assertThat(bucket.getName()).as("Bucket name should end with " + TEST_BUCKET_NAME)
        .isEqualTo(TEST_BUCKET_NAME);
  }

  /**
   * Checks if a bucket can be deleted.
   *
   * @throws Exception if an Exception occurred.
   */
  @Test
  void shouldDeleteBucket() throws Exception {
    bucketStore.createBucket(TEST_BUCKET_NAME);
    boolean bucketDeleted = bucketStore.deleteBucket(TEST_BUCKET_NAME);
    Bucket bucket = bucketStore.getBucket(TEST_BUCKET_NAME);

    assertThat(bucketDeleted).as("Deletion should succeed!").isTrue();
    assertThat(bucket).as("Bucket should be null!").isNull();
  }

  /**
   * Deletes all existing buckets.
   *
   * @throws Exception if bucket could not be deleted.
   */
  @AfterEach
  void cleanupBucketStore() throws Exception {
    for (final Bucket bucket : bucketStore.listBuckets()) {
      bucketStore.deleteBucket(bucket.getName());
    }
  }
}
