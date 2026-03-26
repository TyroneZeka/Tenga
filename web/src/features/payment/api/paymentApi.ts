import { api } from '@/lib/api'
import type { InitiatePaymentRequest, Transaction, TransactionPage } from '../types'

function toBackendBody(data: InitiatePaymentRequest) {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const { method: _method, ...body } = data
  return body
}

export async function initiateEcoCash(data: InitiatePaymentRequest): Promise<Transaction> {
  const res = await api.post('/payments/ecocash', toBackendBody(data))
  return res.data
}

export async function initiateInnBucks(data: InitiatePaymentRequest): Promise<Transaction> {
  const res = await api.post('/payments/innbucks', toBackendBody(data))
  return res.data
}

export async function getTransactions(): Promise<TransactionPage> {
  const res = await api.get('/payments/transactions')
  return res.data
}
