// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////

package com.google.crypto.tink.tinkey;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.crypto.tink.proto.KeyTemplate;
import com.google.protobuf.TextFormat;
import java.io.OutputStream;

/**
 * Creates a new {@link KeyTemplate}.
 */
public class CreateKeyTemplateCommand extends CreateKeyTemplateOptions implements Command {
  @Override
  public void run() throws Exception {
    validate();
    create(outputStream, typeUrlValue, keyFormatValue);
  }

  /**
   * Creates a new {@link KeyTemplate}.
   *
   * <p>The new key template contains a key of type {@code typeUrlValue} and a value of
   * {@code keyFormatValue}.
   */
  public static void create(OutputStream outputStream, String typeUrlValue,
      String keyFormatValue) throws Exception {
    KeyTemplate keyTemplate;
    if (keyFormatValue != null) {
      keyTemplate = TinkeyUtil.createKeyTemplateFromText(typeUrlValue, keyFormatValue);
    } else {
      keyTemplate = KeyTemplate.newBuilder().setTypeUrl(typeUrlValue).build();
    }
    String comment = "# Format: KeyTemplate in text format, "
        + "see https://github.com/google/tink/blob/master/proto/tink.proto\n"
        + "# Generated with command:\n"
        + "#     tinkey create-key-template \\\n"
        + String.format("#     --type-url %s \\\n", typeUrlValue);
    if (keyFormatValue != null) {
      comment += String.format("#     --key-format \"%s\"\n", keyFormatValue);
    }

    outputStream.write(comment.getBytes(UTF_8));
    byte[] output = TextFormat.printToUnicodeString(keyTemplate).getBytes(UTF_8);
    outputStream.write(output);
  }
}
