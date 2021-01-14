/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.malsolo.kafka.streams.trading.model;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class StockTransaction extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1805057325483843333L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"StockTransaction\",\"namespace\":\"com.malsolo.kafka.streams.trading.model\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"customerId\",\"type\":\"string\"},{\"name\":\"companyId\",\"type\":\"string\"},{\"name\":\"shares\",\"type\":\"int\"},{\"name\":\"transactionTimestamp\",\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<StockTransaction> ENCODER =
      new BinaryMessageEncoder<StockTransaction>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<StockTransaction> DECODER =
      new BinaryMessageDecoder<StockTransaction>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<StockTransaction> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<StockTransaction> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<StockTransaction> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<StockTransaction>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this StockTransaction to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a StockTransaction from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a StockTransaction instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static StockTransaction fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private long id;
   private java.lang.CharSequence customerId;
   private java.lang.CharSequence companyId;
   private int shares;
   private long transactionTimestamp;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public StockTransaction() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param customerId The new value for customerId
   * @param companyId The new value for companyId
   * @param shares The new value for shares
   * @param transactionTimestamp The new value for transactionTimestamp
   */
  public StockTransaction(java.lang.Long id, java.lang.CharSequence customerId, java.lang.CharSequence companyId, java.lang.Integer shares, java.lang.Long transactionTimestamp) {
    this.id = id;
    this.customerId = customerId;
    this.companyId = companyId;
    this.shares = shares;
    this.transactionTimestamp = transactionTimestamp;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return customerId;
    case 2: return companyId;
    case 3: return shares;
    case 4: return transactionTimestamp;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: customerId = (java.lang.CharSequence)value$; break;
    case 2: companyId = (java.lang.CharSequence)value$; break;
    case 3: shares = (java.lang.Integer)value$; break;
    case 4: transactionTimestamp = (java.lang.Long)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'customerId' field.
   * @return The value of the 'customerId' field.
   */
  public java.lang.CharSequence getCustomerId() {
    return customerId;
  }


  /**
   * Sets the value of the 'customerId' field.
   * @param value the value to set.
   */
  public void setCustomerId(java.lang.CharSequence value) {
    this.customerId = value;
  }

  /**
   * Gets the value of the 'companyId' field.
   * @return The value of the 'companyId' field.
   */
  public java.lang.CharSequence getCompanyId() {
    return companyId;
  }


  /**
   * Sets the value of the 'companyId' field.
   * @param value the value to set.
   */
  public void setCompanyId(java.lang.CharSequence value) {
    this.companyId = value;
  }

  /**
   * Gets the value of the 'shares' field.
   * @return The value of the 'shares' field.
   */
  public int getShares() {
    return shares;
  }


  /**
   * Sets the value of the 'shares' field.
   * @param value the value to set.
   */
  public void setShares(int value) {
    this.shares = value;
  }

  /**
   * Gets the value of the 'transactionTimestamp' field.
   * @return The value of the 'transactionTimestamp' field.
   */
  public long getTransactionTimestamp() {
    return transactionTimestamp;
  }


  /**
   * Sets the value of the 'transactionTimestamp' field.
   * @param value the value to set.
   */
  public void setTransactionTimestamp(long value) {
    this.transactionTimestamp = value;
  }

  /**
   * Creates a new StockTransaction RecordBuilder.
   * @return A new StockTransaction RecordBuilder
   */
  public static com.malsolo.kafka.streams.trading.model.StockTransaction.Builder newBuilder() {
    return new com.malsolo.kafka.streams.trading.model.StockTransaction.Builder();
  }

  /**
   * Creates a new StockTransaction RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new StockTransaction RecordBuilder
   */
  public static com.malsolo.kafka.streams.trading.model.StockTransaction.Builder newBuilder(com.malsolo.kafka.streams.trading.model.StockTransaction.Builder other) {
    if (other == null) {
      return new com.malsolo.kafka.streams.trading.model.StockTransaction.Builder();
    } else {
      return new com.malsolo.kafka.streams.trading.model.StockTransaction.Builder(other);
    }
  }

  /**
   * Creates a new StockTransaction RecordBuilder by copying an existing StockTransaction instance.
   * @param other The existing instance to copy.
   * @return A new StockTransaction RecordBuilder
   */
  public static com.malsolo.kafka.streams.trading.model.StockTransaction.Builder newBuilder(com.malsolo.kafka.streams.trading.model.StockTransaction other) {
    if (other == null) {
      return new com.malsolo.kafka.streams.trading.model.StockTransaction.Builder();
    } else {
      return new com.malsolo.kafka.streams.trading.model.StockTransaction.Builder(other);
    }
  }

  /**
   * RecordBuilder for StockTransaction instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<StockTransaction>
    implements org.apache.avro.data.RecordBuilder<StockTransaction> {

    private long id;
    private java.lang.CharSequence customerId;
    private java.lang.CharSequence companyId;
    private int shares;
    private long transactionTimestamp;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.malsolo.kafka.streams.trading.model.StockTransaction.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.customerId)) {
        this.customerId = data().deepCopy(fields()[1].schema(), other.customerId);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.companyId)) {
        this.companyId = data().deepCopy(fields()[2].schema(), other.companyId);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.shares)) {
        this.shares = data().deepCopy(fields()[3].schema(), other.shares);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.transactionTimestamp)) {
        this.transactionTimestamp = data().deepCopy(fields()[4].schema(), other.transactionTimestamp);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
    }

    /**
     * Creates a Builder by copying an existing StockTransaction instance
     * @param other The existing instance to copy.
     */
    private Builder(com.malsolo.kafka.streams.trading.model.StockTransaction other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.customerId)) {
        this.customerId = data().deepCopy(fields()[1].schema(), other.customerId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.companyId)) {
        this.companyId = data().deepCopy(fields()[2].schema(), other.companyId);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.shares)) {
        this.shares = data().deepCopy(fields()[3].schema(), other.shares);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.transactionTimestamp)) {
        this.transactionTimestamp = data().deepCopy(fields()[4].schema(), other.transactionTimestamp);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'customerId' field.
      * @return The value.
      */
    public java.lang.CharSequence getCustomerId() {
      return customerId;
    }


    /**
      * Sets the value of the 'customerId' field.
      * @param value The value of 'customerId'.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder setCustomerId(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.customerId = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'customerId' field has been set.
      * @return True if the 'customerId' field has been set, false otherwise.
      */
    public boolean hasCustomerId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'customerId' field.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder clearCustomerId() {
      customerId = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'companyId' field.
      * @return The value.
      */
    public java.lang.CharSequence getCompanyId() {
      return companyId;
    }


    /**
      * Sets the value of the 'companyId' field.
      * @param value The value of 'companyId'.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder setCompanyId(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.companyId = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'companyId' field has been set.
      * @return True if the 'companyId' field has been set, false otherwise.
      */
    public boolean hasCompanyId() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'companyId' field.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder clearCompanyId() {
      companyId = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'shares' field.
      * @return The value.
      */
    public int getShares() {
      return shares;
    }


    /**
      * Sets the value of the 'shares' field.
      * @param value The value of 'shares'.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder setShares(int value) {
      validate(fields()[3], value);
      this.shares = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'shares' field has been set.
      * @return True if the 'shares' field has been set, false otherwise.
      */
    public boolean hasShares() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'shares' field.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder clearShares() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'transactionTimestamp' field.
      * @return The value.
      */
    public long getTransactionTimestamp() {
      return transactionTimestamp;
    }


    /**
      * Sets the value of the 'transactionTimestamp' field.
      * @param value The value of 'transactionTimestamp'.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder setTransactionTimestamp(long value) {
      validate(fields()[4], value);
      this.transactionTimestamp = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'transactionTimestamp' field has been set.
      * @return True if the 'transactionTimestamp' field has been set, false otherwise.
      */
    public boolean hasTransactionTimestamp() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'transactionTimestamp' field.
      * @return This builder.
      */
    public com.malsolo.kafka.streams.trading.model.StockTransaction.Builder clearTransactionTimestamp() {
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public StockTransaction build() {
      try {
        StockTransaction record = new StockTransaction();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.customerId = fieldSetFlags()[1] ? this.customerId : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.companyId = fieldSetFlags()[2] ? this.companyId : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.shares = fieldSetFlags()[3] ? this.shares : (java.lang.Integer) defaultValue(fields()[3]);
        record.transactionTimestamp = fieldSetFlags()[4] ? this.transactionTimestamp : (java.lang.Long) defaultValue(fields()[4]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<StockTransaction>
    WRITER$ = (org.apache.avro.io.DatumWriter<StockTransaction>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<StockTransaction>
    READER$ = (org.apache.avro.io.DatumReader<StockTransaction>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.id);

    out.writeString(this.customerId);

    out.writeString(this.companyId);

    out.writeInt(this.shares);

    out.writeLong(this.transactionTimestamp);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readLong();

      this.customerId = in.readString(this.customerId instanceof Utf8 ? (Utf8)this.customerId : null);

      this.companyId = in.readString(this.companyId instanceof Utf8 ? (Utf8)this.companyId : null);

      this.shares = in.readInt();

      this.transactionTimestamp = in.readLong();

    } else {
      for (int i = 0; i < 5; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.customerId = in.readString(this.customerId instanceof Utf8 ? (Utf8)this.customerId : null);
          break;

        case 2:
          this.companyId = in.readString(this.companyId instanceof Utf8 ? (Utf8)this.companyId : null);
          break;

        case 3:
          this.shares = in.readInt();
          break;

        case 4:
          this.transactionTimestamp = in.readLong();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










