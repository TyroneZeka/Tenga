export type PaymentMethod = 'ECOCASH' | 'INNBUCKS'

export interface InitiatePaymentRequest {
  listingId: string
  sellerId: string
  amount: number
  currency: 'ZIG' | 'USD'
  payerPhone: string
  /** Routing only — not sent to backend; determines which endpoint to call */
  method: PaymentMethod
}

export interface Transaction {
  id: string
  listingId: string
  buyerId: string
  sellerId: string
  amount: number
  currency: 'ZIG' | 'USD'
  paymentMethod: PaymentMethod
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED' | 'REFUNDED'
  gatewayReference: string | null
  createdAt: string
  completedAt: string | null
}

export interface TransactionPage {
  content: Transaction[]
  totalElements: number
  totalPages: number
  number: number
  size: number
  first: boolean
  last: boolean
}
